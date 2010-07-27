/*******************************************************************************
 * Copyright (c) 2010 BestSolution.at and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of:
 * 
 * a) EPL
 * The Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * b) EDL
 * The Eclipse Development License v1.0 which accompanies this distribution, 
 * is reproduced below, and available at http://www.eclipse.org/org/documents/edl-v10.php
 *
 * Contributors:
 *     Tom Schindl <tom.schindl@bestsolution.at> - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.demo.mailapp.test;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.e4.demo.mailapp.FolderView;
import org.eclipse.e4.demo.mailapp.mailservice.domain.IAccount;
import org.eclipse.e4.demo.mailapp.mailservice.mock.MailSessionFactoryImpl;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class TestFolderView {
	public static void main(String[] args) {
		final Display d = new Display();
		Realm.runWithDefault(SWTObservables.getRealm(d), new Runnable() {
			
			public void run() {
				Shell shell = new Shell(d);
				shell.setLayout(new FillLayout());
				FolderView view = new FolderView(shell, null);
				view.setFolder(((IAccount)new MailSessionFactoryImpl().openSession("", "john", "doe").getAccounts().get(0)).getFolders().get(0));
				
				shell.open();
				
				while( !shell.isDisposed() ) {
					if( ! d.readAndDispatch() ) {
						d.sleep();
					}
				}				
			}
		});
		
		d.dispose();
	}
}
