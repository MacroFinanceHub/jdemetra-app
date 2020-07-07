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
 * @param <R> the type of the resource
 */
public abstract class Configurator<R> {

    @NonNull
    abstract public Config getConfig(@NonNull R resource);

    abstract public void setConfig(@NonNull R resource, @NonNull Config config) throws IllegalArgumentException;

    @NonNull
    abstract public Config editConfig(@NonNull Config config) throws IllegalArgumentException;

    @NonNull
    public IConfigurable toConfigurable(@NonNull R resource) {
        return new ConfigurableImpl(this, resource);
    }

    //<editor-fold defaultstate="collapsed" desc="Internal implementation">
    private static final class ConfigurableImpl<R> implements IConfigurable {

        private final Configurator<R> support;
        private final R resource;

        public ConfigurableImpl(Configurator<R> support, R resource) {
            this.support = support;
            this.resource = resource;
        }

        @Override
        public Config getConfig() {
            return support.getConfig(resource);
        }

        @Override
        public void setConfig(Config config) throws IllegalArgumentException {
            support.setConfig(resource, config);
        }

        @Override
        public Config editConfig(Config config) throws IllegalArgumentException {
            return support.editConfig(config);
        }
    }
    //</editor-fold>
}
