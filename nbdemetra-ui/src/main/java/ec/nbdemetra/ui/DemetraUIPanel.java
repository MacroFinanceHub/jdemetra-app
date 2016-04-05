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
package ec.nbdemetra.ui;

import ec.nbdemetra.ui.ns.AbstractNamedService;
import ec.nbdemetra.ui.ns.INamedService;
import ec.nbdemetra.ui.properties.DataFormatComponent2;
import ec.nbdemetra.ui.tsaction.ITsAction;
import ec.tss.Ts;
import ec.ui.ATsChart;
import ec.ui.DemoUtils;
import ec.ui.interfaces.ITsCollectionView.TsUpdateMode;
import ec.util.chart.ColorScheme;
import ec.util.chart.swing.ColorSchemeIcon;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.stream.Collectors;
import org.openide.explorer.ExplorerManager;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;

final class DemetraUIPanel extends javax.swing.JPanel implements VetoableChangeListener, PropertyChangeListener {

    private final DemetraUIOptionsPanelController controller;
    final ATsChart colorSchemePreviewer;

    DemetraUIPanel(DemetraUIOptionsPanelController controller) {
        this.controller = controller;
        initComponents();

        colorSchemePreviewer = ComponentFactory.getDefault().newTsChart();
        colorSchemePreviewer.setTsCollection(DemoUtils.randomTsCollection(3));
        colorSchemePreviewer.setTsAction(new PreviewTsAction());
        colorSchemePreviewer.setTsUpdateMode(TsUpdateMode.None);
        chartPreviewPanel.add(colorSchemePreviewer);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        chartsPanel = new javax.swing.JPanel();
        chartPreviewPanel = new javax.swing.JPanel();
        dataFormatComponent = new ec.nbdemetra.ui.properties.DataFormatComponent2();
        colorSchemeChoicePanel = new ec.nbdemetra.ui.ns.NamedServiceChoicePanel();
        dataFormatLabel = new javax.swing.JLabel();
        colorSchemeLabel = new javax.swing.JLabel();
        growthChartsPanel = new javax.swing.JPanel();
        growthLastYears = new javax.swing.JSpinner();
        lastYearsLabel = new javax.swing.JLabel();
        htmlFontPanel = new javax.swing.JPanel();
        smallRadio = new javax.swing.JRadioButton();
        normalRadio = new javax.swing.JRadioButton();
        bigRadio = new javax.swing.JRadioButton();
        popupMenuPanel = new javax.swing.JPanel();
        iconsVisibleCB = new javax.swing.JCheckBox();

        chartsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(DemetraUIPanel.class, "DemetraUIPanel.chartsPanel.border.title"))); // NOI18N

        chartPreviewPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        chartPreviewPanel.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(dataFormatLabel, org.openide.util.NbBundle.getMessage(DemetraUIPanel.class, "DemetraUIPanel.dataFormatLabel.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(colorSchemeLabel, org.openide.util.NbBundle.getMessage(DemetraUIPanel.class, "DemetraUIPanel.colorSchemeLabel.text")); // NOI18N

        javax.swing.GroupLayout chartsPanelLayout = new javax.swing.GroupLayout(chartsPanel);
        chartsPanel.setLayout(chartsPanelLayout);
        chartsPanelLayout.setHorizontalGroup(
            chartsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chartsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(chartsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(chartsPanelLayout.createSequentialGroup()
                        .addComponent(colorSchemeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(colorSchemeChoicePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(chartsPanelLayout.createSequentialGroup()
                        .addGroup(chartsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(chartsPanelLayout.createSequentialGroup()
                                .addComponent(dataFormatLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(dataFormatComponent, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(chartPreviewPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 497, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 43, Short.MAX_VALUE))))
        );
        chartsPanelLayout.setVerticalGroup(
            chartsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chartsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(chartsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(colorSchemeChoicePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(colorSchemeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chartPreviewPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(chartsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dataFormatComponent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dataFormatLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        growthChartsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(DemetraUIPanel.class, "DemetraUIPanel.growthChartsPanel.border.title"))); // NOI18N

        growthLastYears.setModel(new javax.swing.SpinnerNumberModel(4, 1, 15, 1));

        org.openide.awt.Mnemonics.setLocalizedText(lastYearsLabel, org.openide.util.NbBundle.getMessage(DemetraUIPanel.class, "DemetraUIPanel.lastYearsLabel.text")); // NOI18N

        javax.swing.GroupLayout growthChartsPanelLayout = new javax.swing.GroupLayout(growthChartsPanel);
        growthChartsPanel.setLayout(growthChartsPanelLayout);
        growthChartsPanelLayout.setHorizontalGroup(
            growthChartsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(growthChartsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lastYearsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(growthLastYears, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        growthChartsPanelLayout.setVerticalGroup(
            growthChartsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(growthChartsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lastYearsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(growthLastYears, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        htmlFontPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(DemetraUIPanel.class, "DemetraUIPanel.htmlFontPanel.border.title"))); // NOI18N

        buttonGroup1.add(smallRadio);
        org.openide.awt.Mnemonics.setLocalizedText(smallRadio, org.openide.util.NbBundle.getMessage(DemetraUIPanel.class, "DemetraUIPanel.smallRadio.text")); // NOI18N
        smallRadio.setEnabled(false);
        smallRadio.setFocusPainted(false);

        buttonGroup1.add(normalRadio);
        normalRadio.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(normalRadio, org.openide.util.NbBundle.getMessage(DemetraUIPanel.class, "DemetraUIPanel.normalRadio.text")); // NOI18N
        normalRadio.setFocusPainted(false);

        buttonGroup1.add(bigRadio);
        org.openide.awt.Mnemonics.setLocalizedText(bigRadio, org.openide.util.NbBundle.getMessage(DemetraUIPanel.class, "DemetraUIPanel.bigRadio.text")); // NOI18N
        bigRadio.setEnabled(false);
        bigRadio.setFocusPainted(false);

        javax.swing.GroupLayout htmlFontPanelLayout = new javax.swing.GroupLayout(htmlFontPanel);
        htmlFontPanel.setLayout(htmlFontPanelLayout);
        htmlFontPanelLayout.setHorizontalGroup(
            htmlFontPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(htmlFontPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(smallRadio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(normalRadio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bigRadio)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        htmlFontPanelLayout.setVerticalGroup(
            htmlFontPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(htmlFontPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(smallRadio)
                .addComponent(normalRadio)
                .addComponent(bigRadio))
        );

        popupMenuPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(DemetraUIPanel.class, "DemetraUIPanel.popupMenuPanel.border.title"))); // NOI18N
        popupMenuPanel.setPreferredSize(new java.awt.Dimension(562, 46));

        org.openide.awt.Mnemonics.setLocalizedText(iconsVisibleCB, org.openide.util.NbBundle.getMessage(DemetraUIPanel.class, "DemetraUIPanel.iconsVisibleCB.text")); // NOI18N
        iconsVisibleCB.setFocusPainted(false);

        javax.swing.GroupLayout popupMenuPanelLayout = new javax.swing.GroupLayout(popupMenuPanel);
        popupMenuPanel.setLayout(popupMenuPanelLayout);
        popupMenuPanelLayout.setHorizontalGroup(
            popupMenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(popupMenuPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(iconsVisibleCB)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        popupMenuPanelLayout.setVerticalGroup(
            popupMenuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(popupMenuPanelLayout.createSequentialGroup()
                .addComponent(iconsVisibleCB)
                .addGap(0, 1, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(chartsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(growthChartsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(htmlFontPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(popupMenuPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(chartsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(growthChartsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(htmlFontPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(popupMenuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    void load() {
        DemetraUI demetraUI = DemetraUI.getDefault();
        colorSchemeChoicePanel.setContent(demetraUI.getColorSchemes().stream().map(o -> new ColorSchemeNamedService(o)).collect(Collectors.toList()));
        colorSchemeChoicePanel.getExplorerManager().addVetoableChangeListener(this);
        colorSchemeChoicePanel.setSelectedServiceName(demetraUI.getColorSchemeName());
        dataFormatComponent.setDataFormat(demetraUI.getDataFormat());
        dataFormatComponent.addPropertyChangeListener(this);
        growthLastYears.setValue(demetraUI.getGrowthLastYears());
        iconsVisibleCB.setSelected(demetraUI.getPopupMenuIconsVisible());
    }

    void store() {
        DemetraUI demetraUI = DemetraUI.getDefault();
        demetraUI.setColorSchemeName(colorSchemeChoicePanel.getSelectedServiceName());
        demetraUI.setDataFormat(dataFormatComponent.getDataFormat());
        demetraUI.setGrowthLastYears((Integer)growthLastYears.getValue());
        demetraUI.setPopupMenuIconsVisible(iconsVisibleCB.isSelected());
    }

    boolean valid() {
        // TODO check whether form is consistent and complete
        return true;
    }

    @Override
    public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
        if (ExplorerManager.PROP_SELECTED_NODES.equals(evt.getPropertyName())) {
            Node[] nodes = (Node[]) evt.getNewValue();
            if (nodes.length > 0) {
                ColorSchemeNamedService tmp = nodes[0].getLookup().lookup(ColorSchemeNamedService.class);
                colorSchemePreviewer.setColorScheme(tmp.getColorScheme());
                colorSchemePreviewer.setTitle(tmp.getDisplayName());
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(DataFormatComponent2.DATA_FORMAT_PROPERTY) && evt.getNewValue() != null) {
            colorSchemePreviewer.setDataFormat(dataFormatComponent.getDataFormat());
        }
    }

    DataFormatComponent2 getDataFormatComponent() {
        return dataFormatComponent;
    }

    private class PreviewTsAction extends AbstractNamedService implements ITsAction {

        PreviewTsAction() {
            super(ITsAction.class, "PreviewTsAction");
        }

        @Override
        public void open(Ts ts) {
            //String displayName = tsActionChoicePanel.getExplorerManager().getSelectedNodes()[0].getDisplayName();
            //String msg = "This would have opened the selected time series\nby using '" + displayName + "' TsAction.";
            //NotifyDescriptor d = new NotifyDescriptor.Message(msg, NotifyDescriptor.INFORMATION_MESSAGE);
            //DialogDisplayer.getDefault().notify(d);
        }
    }

    static class ColorSchemeNamedService extends AbstractNamedService {

        private final ColorScheme colorScheme;

        public ColorSchemeNamedService(ColorScheme colorScheme) {
            super(INamedService.class, colorScheme.getName());
            this.colorScheme = colorScheme;
        }

        @Override
        public String getDisplayName() {
            return colorScheme.getDisplayName();
        }

        @Override
        public Image getIcon(int type, boolean opened) {
            return ImageUtilities.icon2Image(new ColorSchemeIcon(colorScheme));
        }

        public ColorScheme getColorScheme() {
            return colorScheme;
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton bigRadio;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel chartPreviewPanel;
    private javax.swing.JPanel chartsPanel;
    private ec.nbdemetra.ui.ns.NamedServiceChoicePanel colorSchemeChoicePanel;
    private javax.swing.JLabel colorSchemeLabel;
    private ec.nbdemetra.ui.properties.DataFormatComponent2 dataFormatComponent;
    private javax.swing.JLabel dataFormatLabel;
    private javax.swing.JPanel growthChartsPanel;
    private javax.swing.JSpinner growthLastYears;
    private javax.swing.JPanel htmlFontPanel;
    private javax.swing.JCheckBox iconsVisibleCB;
    private javax.swing.JLabel lastYearsLabel;
    private javax.swing.JRadioButton normalRadio;
    private javax.swing.JPanel popupMenuPanel;
    private javax.swing.JRadioButton smallRadio;
    // End of variables declaration//GEN-END:variables
}
