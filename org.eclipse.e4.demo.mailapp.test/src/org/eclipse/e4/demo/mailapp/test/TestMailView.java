package org.eclipse.e4.demo.mailapp.test;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.e4.demo.mailapp.MailView;
import org.eclipse.e4.demo.mailapp.mailservice.domain.IAccount;
import org.eclipse.e4.demo.mailapp.mailservice.domain.IFolder;
import org.eclipse.e4.demo.mailapp.mailservice.mock.MailSessionFactoryImpl;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class TestMailView {
	public static void main(String[] args) {
		final Display d = new Display();
		Realm.runWithDefault(SWTObservables.getRealm(d), new Runnable() {
			
			public void run() {
				Shell shell = new Shell(d);
				shell.setLayout(new FillLayout());
				MailView view = new MailView(shell);
				IFolder folder = ((IAccount)new MailSessionFactoryImpl().openSession("", "john", "doe").getAccounts().get(0)).getFolders().get(0);
				
				view.setMail(folder.getSession().getMails(folder, 0, 1).get(0));
				
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
