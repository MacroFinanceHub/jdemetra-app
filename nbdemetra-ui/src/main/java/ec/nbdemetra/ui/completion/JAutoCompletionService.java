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
package ec.nbdemetra.ui.completion;

import ec.tstoolkit.design.ServiceDefinition;
import ec.util.completion.swing.JAutoCompletion;
import org.checkerframework.checker.nullness.qual.NonNull;
import javax.swing.text.JTextComponent;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Philippe Charles
 * @since 1.3.2
 */
@ServiceDefinition
public abstract class JAutoCompletionService {

    @NonNull
    abstract public JAutoCompletion bind(@NonNull JTextComponent textComponent);
    //
    public static final String LOCALE_PATH = "JAutoCompletionService/Locale";
    public static final String DATE_PATTERN_PATH = "JAutoCompletionService/DatePattern";
    public static final String COLOR_SCHEME_PATH = "JAutoCompletionService/ColorScheme";
    public static final String CHARSET_PATH = "JAutoCompletionService/Charset";

    @NonNull
    public static JAutoCompletion forPathBind(@NonNull String path, @NonNull JTextComponent textComponent) {
        JAutoCompletionService o = Lookups.forPath(path).lookup(JAutoCompletionService.class);
        return o != null ? o.bind(textComponent) : new JAutoCompletion(textComponent);
    }
}
