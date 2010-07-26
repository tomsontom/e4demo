/*******************************************************************************
 * Copyright (c) 2010 BestSolution.at and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tom Schindl <tom.schindl@bestsolution.at> - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.demo.mailapp.newmail;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.demo.mailapp.mailservice.domain.IAccount;
import org.eclipse.e4.demo.mailapp.mailservice.domain.IMail;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.IPresentationEngine;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

@SuppressWarnings("restriction")
public class SendMail {
	@Execute
	public void sendMail(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell, MTrimmedWindow window, IMail mail, IAccount account, IPresentationEngine engine) {
		if( mail.getTo() == null || mail.getTo().trim().length() == 0 || mail.getTo().indexOf('@') == -1 ) {
			MessageDialog.openError(shell, "No Recipient", "Your mail has no recipient.");
			return;
		}
		
		if( mail.getSubject() == null || mail.getSubject().trim().length() == 0 ) {
			if( ! MessageDialog.openQuestion(shell, "No subject", "You have not set a subject would you like to proceed?") ) {
				return;
			}
		}
		
		account.getSession().sendMail(account, mail);
		
		// Bug in 4.0 we need to tear down through the presentation-engine
		engine.removeGui(window);
		window.getParent().getChildren().remove(window);
	}
	
	@CanExecute
	public boolean canExecute(@Optional IAccount account, @Optional IMail mail) {
		return account != null && mail != null;
	}
}
