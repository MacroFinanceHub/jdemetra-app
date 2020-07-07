/*
 * Copyright 2013 National Bank of Belgium
 * 
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved 
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

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 *
 * @author Philippe Charles
 * @param <B> the type of the bean
 */
public abstract class AbstractConfigurable<B> implements IConfigurable {

    @NonNull
    protected abstract B loadBean();

    protected abstract void storeBean(@NonNull B bean);

    protected abstract boolean editBean(@NonNull B bean);

    @NonNull
    protected abstract Config toConfig(@NonNull B bean);

    @NonNull
    protected abstract B fromConfig(@NonNull Config config) throws IllegalArgumentException;

    @Override
    final public Config getConfig() {
        return toConfig(loadBean());
    }

    @Override
    final public void setConfig(Config config) throws IllegalArgumentException {
        storeBean(fromConfig(config));
    }

    @Override
    final public Config editConfig(Config config) throws IllegalArgumentException {
        B bean = fromConfig(config);
        if (editBean(bean)) {
            return toConfig(bean);
        }
        return config;
    }
}
