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
package ec.nbdemetra.ui.nodes;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import ec.tstoolkit.design.UtilityClass;
import ec.tstoolkit.utilities.Trees;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;
import org.checkerframework.checker.nullness.qual.NonNull;
import javax.swing.Action;
import org.openide.nodes.Node;
import org.openide.util.Utilities;

/**
 * Convenient methods to deal with nodes.
 *
 * @author Philippe Charles
 */
@UtilityClass(Node.class)
public final class Nodes {

    private Nodes() {
        // static class
    }

    @NonNull
    public static Action[] actionsForPath(@NonNull String path) {
        return Iterables.toArray(Utilities.actionsForPath(path), Action.class);
    }

    @NonNull
    public static Stream<Node> childrenStream(@NonNull Node root) {
        return !root.isLeaf()
                ? Arrays.stream(root.getChildren().getNodes())
                : Stream.empty();
    }

    @NonNull
    public static FluentIterable<Node> childrenIterable(@NonNull Node root) {
        return FluentIterable.from(root.isLeaf() ? Collections.emptyList() : Arrays.asList(root.getChildren().getNodes()));
    }

    @NonNull
    public static FluentIterable<Node> breadthFirstIterable(@NonNull Node root) {
        return FluentIterable.from(root.isLeaf() ? Collections.singleton(root) : Trees.breadthFirstIterable(root, Nodes::childrenStream));
    }

    @NonNull
    public static FluentIterable<Node> depthFirstIterable(@NonNull Node root) {
        return FluentIterable.from(root.isLeaf() ? Collections.singleton(root) : Trees.depthFirstIterable(root, Nodes::childrenStream));
    }

    @NonNull
    public static FluentIterable<Node> asIterable(@NonNull Node[] nodes) {
        return FluentIterable.from(Arrays.asList(nodes));
    }
}
