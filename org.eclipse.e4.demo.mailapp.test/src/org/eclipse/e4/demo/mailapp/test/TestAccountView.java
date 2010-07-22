package org.eclipse.e4.demo.mailapp.test;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.e4.demo.mailapp.AccountView;
import org.eclipse.e4.demo.mailapp.mailservice.mock.MailSessionFactoryImpl;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class TestAccountView {
	public static void main(String[] args) {
		final Display d = new Display();
		Realm.runWithDefault(SWTObservables.getRealm(d), new Runnable() {
			
			public void run() {
				Shell shell = new Shell(d);
				shell.setLayout(new FillLayout());
				AccountView view = new AccountView(shell);
				view.setFactory(new MailSessionFactoryImpl());
				view.setUsername("john");
				view.setPassword("doe");
				view.setHost("tomsondev.bestsolution.at");
				view.init();
				
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
