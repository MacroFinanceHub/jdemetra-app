/*
 * Copyright 2013 National Bank of Belgium
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they will be approved 
 * by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and 
 * limitations under the Licence.
 */
package ec.ui.chart;

import com.google.common.base.Converter;
import com.google.common.base.Preconditions;
import ec.nbdemetra.ui.BeanHandler;
import ec.nbdemetra.ui.Config;
import ec.nbdemetra.ui.Configurator;
import ec.nbdemetra.ui.DemetraUI;
import ec.nbdemetra.ui.IConfigurable;
import ec.nbdemetra.ui.properties.PropertySheetDialogBuilder;
import ec.nbdemetra.ui.awt.ActionMaps;
import ec.nbdemetra.ui.awt.InputMaps;
import ec.nbdemetra.ui.completion.JAutoCompletionService;
import ec.nbdemetra.ui.properties.IBeanEditor;
import ec.nbdemetra.ui.properties.NodePropertySetBuilder;
import ec.tss.Ts;
import ec.tss.TsCollection;
import ec.tss.datatransfer.TssTransferSupport;
import ec.tss.tsproviders.utils.IParam;
import ec.tss.tsproviders.utils.Params;
import ec.tstoolkit.utilities.Arrays2;
import ec.tstoolkit.utilities.IntList;
import ec.ui.ATsChart;
import ec.ui.DemoUtils;
import ec.ui.interfaces.ITsChart;
import ec.ui.interfaces.ITsCollectionView;
import ec.ui.interfaces.ITsPrinter;
import ec.util.chart.ColorScheme;
import ec.util.chart.ObsFunction;
import ec.util.chart.ObsIndex;
import ec.util.chart.ObsPredicate;
import ec.util.chart.SeriesFunction;
import ec.util.chart.SeriesPredicate;
import ec.util.chart.TimeSeriesChart.Element;
import ec.util.chart.swing.JTimeSeriesChart;
import ec.util.chart.swing.JTimeSeriesChartCommand;
import ec.util.various.swing.FontAwesome;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.beans.Beans;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.TooManyListenersException;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import org.jfree.data.xy.IntervalXYDataset;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;

/**
 * Component used to display time series in a chart. Supports drag and drop,
 * copy/paste.
 *
 * @author Demortier Jeremy
 * @author Philippe Charles
 */
public class JTsChart extends ATsChart implements IConfigurable {

    private static final long serialVersionUID = -4816158139844033936L;
    private static final Configurator<JTsChart> CONFIGURATOR = createConfigurator();

    //<editor-fold defaultstate="collapsed" desc="Properties">
    public static final String HOVERED_OBS_PROPERTY = "hoveredObs";

    private static final ObsIndex DEFAULT_HOVERED_OBS = ObsIndex.NULL;

    private ObsIndex hoveredObs;
    //</editor-fold>

    protected final JTimeSeriesChart chartPanel;
    private final ChartHandler chartHandler;
    protected final ITsPrinter printer;
    protected final DataFeatureModel dataFeatureModel;
    private final TsCollectionSelectionListener selectionListener;
    private final IntList savedSelection;

    public JTsChart() {
        this.hoveredObs = DEFAULT_HOVERED_OBS;

        this.chartPanel = new JTimeSeriesChart();
        this.chartHandler = new ChartHandler();
        this.printer = JTimeSeriesChartUtil.newTsPrinter(chartPanel);
        this.dataFeatureModel = new DataFeatureModel();
        this.selectionListener = new TsCollectionSelectionListener();
        this.savedSelection = new IntList();

        initChart();

        ActionMaps.copyEntries(getActionMap(), false, chartPanel.getActionMap());
        InputMaps.copyEntries(getInputMap(), false, chartPanel.getInputMap());

        enableSeriesSelection();
        enableDropPreview();
        enableOpenOnDoubleClick();
        enableObsHovering();
        enableProperties();

        setLayout(new BorderLayout());
        add(chartPanel, BorderLayout.CENTER);

        if (Beans.isDesignTime()) {
            applyDesignTimeProperties();
        }
    }

    private void initChart() {
        onAxisVisibleChange();
        onColorSchemeChange();
        onLegendVisibleChange();
        onTitleChange();
        onUpdateModeChange();
        onDataFormatChange();
        onTransferHandlerChange();
        onComponentPopupMenuChange();
        chartPanel.setSeriesFormatter(new SeriesFunction<String>() {
            @Override
            public String apply(int series) {
                TsCollection collection = getTsCollection();
                return collection.getCount() > series ? collection.get(series).getName() : chartPanel.getDataset().getSeriesKey(series).toString();
            }
        });
        chartPanel.setObsFormatter(new ObsFunction<String>() {
            @Override
            public String apply(int series, int obs) {
                IntervalXYDataset dataset = chartPanel.getDataset();
                CharSequence period = chartPanel.getPeriodFormat().format(new Date(dataset.getX(series, obs).longValue()));
                CharSequence value = chartPanel.getValueFormat().format(dataset.getY(series, obs));
                StringBuilder result = new StringBuilder();
                result.append(period).append(": ").append(value);
                if (dataFeatureModel.hasFeature(Ts.DataFeature.Forecasts, series, obs)) {
                    result.append("\nForecast");
                }
                return result.toString();
            }
        });
        chartPanel.setDashPredicate(new ObsPredicate() {
            @Override
            public boolean apply(int series, int obs) {
                return dataFeatureModel.hasFeature(Ts.DataFeature.Forecasts, series, obs);
            }
        });
        chartPanel.setLegendVisibilityPredicate(new SeriesPredicate() {
            @Override
            public boolean apply(int series) {
                return series < getTsCollection().getCount();
            }
        });
    }

    private void applyDesignTimeProperties() {
        setTsCollection(DemoUtils.randomTsCollection(3));
        setTsUpdateMode(ITsCollectionView.TsUpdateMode.None);
        setPreferredSize(new Dimension(200, 150));
        setTitle("Chart preview");
    }

    //<editor-fold defaultstate="collapsed" desc="Interactive stuff">
    private void enableSeriesSelection() {
        chartPanel.getSeriesSelectionModel().addListSelectionListener(selectionListener);
    }

    private void enableDropPreview() {
        try {
            chartPanel.getDropTarget().addDropTargetListener(new DropTargetAdapter() {
                @Override
                public void dragEnter(DropTargetDragEvent dtde) {
                    if (!getTsUpdateMode().isReadOnly()) {
                        Transferable t = dtde.getTransferable();
                        if (TssTransferSupport.getDefault().canImport(t)) {
                            Ts[] dropContent = TssTransferSupport.getDefault()
                                    .toTsCollectionStream(t)
                                    .flatMap(TsCollection::stream)
                                    .toArray(Ts[]::new);
                            setDropContent(dropContent != null ? dropContent : null);
                        }
                    }
                }

                @Override
                public void dragExit(DropTargetEvent dte) {
                    setDropContent(null);
                }

                @Override
                public void drop(DropTargetDropEvent dtde) {
                    dragExit(dtde);
                }
            });
        } catch (TooManyListenersException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void enableOpenOnDoubleClick() {
        chartPanel.addMouseListener(new TsActionMouseAdapter());
    }

    private void enableObsHovering() {
        chartPanel.addPropertyChangeListener(chartHandler);
    }

    private void enableProperties() {
        this.addPropertyChangeListener(evt -> {
            switch (evt.getPropertyName()) {
                case HOVERED_OBS_PROPERTY:
                    onHoveredObsChange();
                    break;
                case "transferHandler":
                    onTransferHandlerChange();
                    break;
                case "componentPopupMenu":
                    onComponentPopupMenuChange();
                    break;
            }
        });
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Event Handlers">
    @Override
    protected void onDataFormatChange() {
        JTimeSeriesChartUtil.setDataFormat(chartPanel, themeSupport.getDataFormat());
    }

    @Override
    protected void onColorSchemeChange() {
        chartPanel.setColorSchemeSupport(null);
        chartPanel.setColorSchemeSupport(themeSupport);
    }

    @Override
    protected void onCollectionChange() {
        selectionListener.setEnabled(false);
        Ts[] tss = getTsCollection().toArray();
        dataFeatureModel.setData(tss);
        chartPanel.setDataset(TsXYDatasets.from(tss));
        updateNoDataMessage();
        selectionListener.setEnabled(true);
    }

    /**
     * Redraws all the curves from the chart; unlike redrawAll() it won't cause
     * the chart to lose its zoom level.
     */
    @Override
    protected void onSelectionChange() {
        selectionListener.setEnabled(false);
        selectionListener.changeSelection(chartPanel.getSeriesSelectionModel());
        selectionListener.setEnabled(true);
    }

    @Override
    protected void onUpdateModeChange() {
        updateNoDataMessage();
    }

    @Override
    protected void onTsActionChange() {
        // do nothing
    }

    @Override
    protected void onDropContentChange() {
        TsCollection collection = getTsCollection();
        Ts[] tss = Arrays2.concat(collection.toArray(), dropContent);
        dataFeatureModel.setData(tss);
        chartPanel.setDataset(TsXYDatasets.from(tss));

        selectionListener.setEnabled(false);
        ListSelectionModel m = chartPanel.getSeriesSelectionModel();
        if (dropContent.length > 0) {
            savedSelection.clear();
            for (int series = m.getMinSelectionIndex(); series <= m.getMaxSelectionIndex(); series++) {
                if (m.isSelectedIndex(series)) {
                    savedSelection.add(series);
                }
            }
            int offset = collection.getCount();
            m.setSelectionInterval(offset, offset + dropContent.length);
        } else {
            m.clearSelection();
            for (int series : savedSelection.toArray()) {
                m.addSelectionInterval(series, series);
            }
        }
        selectionListener.setEnabled(true);
    }

    @Override
    protected void onLegendVisibleChange() {
        chartPanel.setElementVisible(Element.LEGEND, legendVisible);
    }

    @Override
    protected void onTitleVisibleChange() {
        chartPanel.setElementVisible(Element.TITLE, titleVisible);
    }

    @Override
    protected void onAxisVisibleChange() {
        chartPanel.setElementVisible(Element.AXIS, axisVisible);
    }

    @Override
    protected void onTitleChange() {
        chartPanel.setTitle(title);
    }

    @Override
    protected void onLinesThicknessChange() {
        chartPanel.setLineThickness(linesThickness == LinesThickness.Thin ? 1f : 2f);
    }

    private void onTransferHandlerChange() {
        TransferHandler th = getTransferHandler();
        chartPanel.setTransferHandler(th != null ? th : new TsCollectionTransferHandler());
    }

    private void onComponentPopupMenuChange() {
        JPopupMenu popupMenu = getComponentPopupMenu();
        chartPanel.setComponentPopupMenu(popupMenu != null ? popupMenu : buildChartMenu().getPopupMenu());
    }

    private void onHoveredObsChange() {
        chartHandler.applyHoveredCell(hoveredObs);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters/Setters">
    @NonNull
    public ObsIndex getHoveredObs() {
        return hoveredObs;
    }

    public void setHoveredObs(@Nullable ObsIndex hoveredObs) {
        ObsIndex old = this.hoveredObs;
        this.hoveredObs = hoveredObs != null ? hoveredObs : DEFAULT_HOVERED_OBS;
        firePropertyChange(HOVERED_OBS_PROPERTY, old, this.hoveredObs);
    }
    //</editor-fold>

    private void updateNoDataMessage() {
        chartPanel.setNoDataMessage(getNoDataMessage(getTsCollection(), getTsUpdateMode()));
    }

    @Override
    protected JMenu buildExportImageMenu() {
        JMenu result = super.buildExportImageMenu();
        JMenuItem clipboard = result.add(JTimeSeriesChartCommand.copyImage().toAction(chartPanel));
        clipboard.setText("Clipboard");
        clipboard.setIcon(demetraUI.getPopupMenuIcon(FontAwesome.FA_CLIPBOARD));
        JMenuItem file = result.add(JTimeSeriesChartCommand.saveImage().toAction(chartPanel));
        file.setText("File...");
        file.setIcon(demetraUI.getPopupMenuIcon(FontAwesome.FA_PICTURE_O));
        return result;
    }

    @Override
    public void showAll() {
        chartPanel.resetZoom();
    }

    @Override
    public ITsPrinter getPrinter() {
        return printer;
    }

    @Override
    public Config getConfig() {
        return CONFIGURATOR.getConfig(this);
    }

    @Override
    public void setConfig(Config config) {
        CONFIGURATOR.setConfig(this, config);
    }

    @Override
    public Config editConfig(Config config) {
        return CONFIGURATOR.editConfig(config);
    }

    public static final class InternalConfig {

        public boolean legendVisible = DEFAULT_LEGENDVISIBLE;
        public boolean titleVisible = DEFAULT_TITLEVISIBLE;
        public boolean axisVisible = DEFAULT_AXISVISIBLE;
        public String title = DEFAULT_TITLE;
        public ITsChart.LinesThickness linesThickness = DEFAULT_LINES_THICKNESS;
        public String colorSchemeName = "";
        public double[] zoom = new double[0];

        ColorScheme getColorScheme() {
            if (colorSchemeName.isEmpty()) {
                return null;
            }
            return DemetraUI.getDefault().getColorSchemes().stream()
                    .filter(o -> colorSchemeName.equals(o.getName()))
                    .findFirst()
                    .orElse(null);
        }
    }

    private final class ChartHandler implements PropertyChangeListener {

        private boolean updating = false;

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (!updating) {
                updating = true;
                switch (evt.getPropertyName()) {
                    case JTimeSeriesChart.HOVERED_OBS_PROPERTY:
                        setHoveredObs(chartPanel.getHoveredObs());
                        break;
                }
                updating = false;
            }
        }

        private void applyHoveredCell(ObsIndex hoveredObs) {
            if (!updating) {
                chartPanel.setHoveredObs(hoveredObs);
            }
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Configuration details">
    private static Configurator<JTsChart> createConfigurator() {
        return new InternalConfigHandler().toConfigurator(new InternalConfigConverter(), new InternalConfigEditor());
    }

    private static final class InternalConfigHandler extends BeanHandler<InternalConfig, JTsChart> {

        @Override
        public InternalConfig loadBean(JTsChart r) {
            ColorScheme colorScheme = r.themeSupport.getLocalColorScheme();
            InternalConfig result = new InternalConfig();
            result.legendVisible = r.legendVisible;
            result.titleVisible = r.titleVisible;
            result.axisVisible = r.axisVisible;
            result.title = r.title;
            result.linesThickness = r.linesThickness;
            result.colorSchemeName = colorScheme != null ? colorScheme.getName() : "";
            result.zoom = r.chartPanel.getZoom();
            return result;
        }

        @Override
        public void storeBean(JTsChart resource, InternalConfig bean) {
            resource.setLegendVisible(bean.legendVisible);
            resource.setTitleVisible(bean.titleVisible);
            resource.setAxisVisible(bean.axisVisible);
            resource.setTitle(bean.title);
            resource.setLinesThickness(bean.linesThickness);
            resource.setColorScheme(bean.getColorScheme());
            resource.chartPanel.setZoom(bean.zoom);
        }
    }

    private static final class InternalConfigEditor implements IBeanEditor {

        @Override
        public boolean editBean(Object bean) throws IntrospectionException {
            Sheet sheet = new Sheet();
            NodePropertySetBuilder b = new NodePropertySetBuilder();

            b.reset("Chart display");
            b.withBoolean().selectField(bean, "legendVisible").display("Show legend").description("Whether the legend should be shown or not").add();
            b.withBoolean().selectField(bean, "titleVisible").display("Show title").description("Whether the title should be shown or not").add();
            b.withBoolean().selectField(bean, "axisVisible").display("Show axis").description("Whether the axis should be shown or not").add();
            b.with(String.class).selectField(bean, "title").display("Title").description("Title of the chart").add();
            sheet.put(b.build());
            b.reset("Series display");
            b.withEnum(ITsChart.LinesThickness.class).selectField(bean, "linesThickness").display("Line thickness").description("Thickness of the line representing the series").add();
            b.withAutoCompletion().selectField(bean, "colorSchemeName").servicePath(JAutoCompletionService.COLOR_SCHEME_PATH).promptText(DemetraUI.getDefault().getColorSchemeName()).display("Color scheme").add();

            sheet.put(b.build());
            return new PropertySheetDialogBuilder().title("Configure chart").editSheet(sheet);
        }
    }

    private static final class InternalConfigConverter extends Converter<InternalConfig, Config> {

        private static final String DOMAIN = JTsChart.class.getName(), NAME = "", VERSION = "";
        private static final IParam<Config, Boolean> LEGEND_VISIBLE = Params.onBoolean(DEFAULT_LEGENDVISIBLE, "legendVisible");
        private static final IParam<Config, Boolean> TITLE_VISIBLE = Params.onBoolean(DEFAULT_TITLEVISIBLE, "titleVisible");
        private static final IParam<Config, Boolean> AXIS_VISIBLE = Params.onBoolean(DEFAULT_AXISVISIBLE, "axisVisible");
        private static final IParam<Config, String> TITLE = Params.onString(DEFAULT_TITLE, "title");
        private static final IParam<Config, ITsChart.LinesThickness> LINES_THICKNESS = Params.onEnum(DEFAULT_LINES_THICKNESS, "linesThickness");
        private static final IParam<Config, String> COLOR_SCHEME_NAME = Params.onString("", "colorSchemeName");
        private static final IParam<Config, double[]> ZOOM = Params.onDoubleArray("zoom", new double[0]);

        @Override
        protected Config doForward(InternalConfig a) {
            Config.Builder b = Config.builder(DOMAIN, NAME, VERSION);
            LEGEND_VISIBLE.set(b, a.legendVisible);
            TITLE_VISIBLE.set(b, a.titleVisible);
            AXIS_VISIBLE.set(b, a.axisVisible);
            TITLE.set(b, a.title);
            LINES_THICKNESS.set(b, a.linesThickness);
            COLOR_SCHEME_NAME.set(b, a.colorSchemeName);
            ZOOM.set(b, a.zoom);
            return b.build();
        }

        @Override
        protected InternalConfig doBackward(Config config) {
            Preconditions.checkArgument(DOMAIN.equals(config.getDomain()), "Not produced here");
            InternalConfig result = new InternalConfig();
            result.legendVisible = LEGEND_VISIBLE.get(config);
            result.titleVisible = TITLE_VISIBLE.get(config);
            result.axisVisible = AXIS_VISIBLE.get(config);
            result.title = TITLE.get(config);
            result.linesThickness = LINES_THICKNESS.get(config);
            result.colorSchemeName = COLOR_SCHEME_NAME.get(config);
            result.zoom = ZOOM.get(config);
            return result;
        }
    }
    //</editor-fold>
}
