/*
 * Copyright 2016 National Bank of Belgium
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
package ec.nbdemetra.sa.output;

import ec.nbdemetra.ui.ns.INamedService;
import java.awt.Image;
import org.openide.nodes.Sheet;

/**
 *
 * @author Jean Palate
 */
public interface INbOutputFactory extends INamedService {

    @Override
    String getName();

    AbstractOutputNode createNode();

    AbstractOutputNode createNodeFor(Object properties);

    @Override
    default String getDisplayName() {
        return getName();
    }

    @Override
    default Image getIcon(int type, boolean opened) {
        return null;
    }

    @Override
    default Sheet createSheet() {
        return new Sheet();
    }
}
