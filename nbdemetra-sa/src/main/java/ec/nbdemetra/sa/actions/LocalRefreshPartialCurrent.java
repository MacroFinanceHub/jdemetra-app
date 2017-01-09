/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.nbdemetra.sa.actions;

import ec.nbdemetra.sa.MultiProcessingManager;
import ec.nbdemetra.sa.SaBatchUI;
import ec.nbdemetra.ui.ActiveViewManager;
import ec.tss.sa.EstimationPolicyType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "SaProcessing",
id = "ec.nbdemetra.sa.actions.LocalRefreshPartialCurrent")
@ActionRegistration(displayName = "#CTL_LocalRefreshPartialCurrent")
@ActionReferences({
    @ActionReference(path = MultiProcessingManager.LOCALPATH + LocalRefreshPartial.PATH, position = 1225)
})
@Messages("CTL_LocalRefreshPartialCurrent=Regression coefficients")
public final class LocalRefreshPartialCurrent implements ActionListener  {

    public LocalRefreshPartialCurrent() {
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        SaBatchUI ui = ActiveViewManager.getInstance().getLookup().lookup(SaBatchUI.class);
        if (ui == null) {
            return;
        }
        ui.refreshSelection(EstimationPolicyType.FixedParameters, false, true);
    }
}