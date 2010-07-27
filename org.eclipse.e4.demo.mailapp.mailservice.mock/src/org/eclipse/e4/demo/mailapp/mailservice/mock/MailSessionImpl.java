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
 * and is available at http://www.eclipse.org/org/documents/edl-v10.php
 *
 * Contributors:
 *     Tom Schindl <tom.schindl@bestsolution.at> - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.demo.mailapp.mailservice.mock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.e4.demo.mailapp.mailservice.IMailSession;
import org.eclipse.e4.demo.mailapp.mailservice.domain.IAccount;
import org.eclipse.e4.demo.mailapp.mailservice.domain.IFolder;
import org.eclipse.e4.demo.mailapp.mailservice.domain.IMail;
import org.eclipse.e4.demo.mailapp.mailservice.domain.impl.AccountImpl;
import org.eclipse.e4.demo.mailapp.mailservice.domain.impl.FolderImpl;
import org.eclipse.e4.demo.mailapp.mailservice.domain.impl.MailImpl;

@SuppressWarnings("restriction")
public class MailSessionImpl implements IMailSession {
	
	private Map<IFolder, List<IMail>> folderMails = Collections.synchronizedMap(new HashMap<IFolder, List<IMail>>());
	
	private WritableList accounts = new WritableList();

	{
		AccountImpl account = new AccountImpl(this);
		account.setName("tom.schindl@bestsolution.at");
		FolderImpl folder = new FolderImpl();
		folder.setName("Inbox");
		account.addFolder(folder);

		List<IMail> mails = new Vector<IMail>();
		IMail mail = new MailImpl();
		mail.setTo("tom.schindl@bestsolution.at");
		mail.setFrom("tom@bestsolution.at");
		mail.setSubject("New e4 demo with tutorial available");
		mail.setBody("Hi,\nIf you want to start developing e4 applications and need a step by step guide how to write an RCP-Application using the e4-runtime-platform. You should take a look at a tutorial I've just published on http://tomsondev.bestsolution.at.");
		try {
			mail.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2010-07-21 16:36"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		mails.add(mail);
		folderMails.put(folder, mails);
		folder.setMailCount(1);
		
		mails = new Vector<IMail>();
		FolderImpl subFolder = new FolderImpl();
		subFolder.setName("e4-dev");
		folder.addFolder(subFolder);
				
		mail = new MailImpl();
		mail.setTo("e4-dev@bestsolution.at");
		mail.setFrom("pwebster@alumni.uwaterloo.ca");
		mail.setSubject("[e4-dev] RC3 rules of engagement.");
		mail.setBody("OK, now that we're in RC3, we need two +1s to make the fix (and technically 2 committers must code review), according to:\n\nhttp://www.eclipse.org/eclipse/development/plans/freeze_plan_4.0.php#FixPassAfterRC2\"July 20-23 - contributions to RC3\"\n\nBoris, is this what we want to do?\n\nLater,\nPW");
		try {
			mail.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2010-07-20 02:23"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		mails.add(mail);
		
		
		mail = new MailImpl();
		mail.setTo("e4-dev@bestsolution.at");
		mail.setFrom("John_Arthorne@ca.ibm.com");
		mail.setSubject("[e4-dev] Fw: e4 Move Review");
		mail.setBody("FYI, the move of components involved in the Eclipse 4.0 release from e4 to Platform has been approved by the EMO. For more details see http://wiki.eclipse.org/E4/4.0_Release_Process.\n\nJohn");
		try {
			mail.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2010-07-19 20:55"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		mails.add(mail);

		folderMails.put(subFolder, mails);
		subFolder.setMailCount(2);
		
		
		folder = new FolderImpl();
		folder.setName("Sent");
		account.addFolder(folder);
		accounts.add(account);

		// ===============================================

		account = new AccountImpl(this);
		account.setName("news@bestsolution.at");
		folder = new FolderImpl();
		folder.setName("Inbox");
		account.addFolder(folder);
		accounts.add(account);
	}

	class MailGenerator implements Runnable {
		private final FolderImpl folder;
		
		MailGenerator(FolderImpl folder) {
			this.folder = folder;
		}
		
		public void run() {
			while( true ) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					break;
				}
				
				Date d = new Date();
				MailImpl mail = new MailImpl();
				mail.setTo("tom.schindl@bestsolution.at");
				mail.setFrom("noreply@bestsolution.at");
				mail.setSubject("Auto Mail " + SimpleDateFormat.getDateTimeInstance().format(d));
				mail.setBody("This is an auto generated mail!");
				mail.setDate(d);
				
				folderMails.get(folder).add(mail);
				folder.setMailCount(folderMails.get(folder).size());
				
				synchronized (listeners) {
					for( ISessionListener l : listeners ) {
						l.mailAdded(folder, mail);
					}
				}
			}
		}
	}
	
	private List<ISessionListener> listeners = new ArrayList<ISessionListener>();
	
	public MailSessionImpl() {
		FolderImpl folder = (FolderImpl) ((IAccount)accounts.get(0)).getFolders().get(0);
		Thread t = new Thread(new MailGenerator(folder));
		t.setDaemon(true);
		t.start();
	}
	
	public IObservableList getAccounts() {
		return accounts;
	}

	public List<IMail> getMails(IFolder folder, int startIndex, int amount) {
		return folderMails.get(folder).subList(startIndex, startIndex + amount);
	}
	
	public void addListener(ISessionListener listener) {
		synchronized(listeners) {
			listeners.add(listener);	
		}
	}
	
	public void removeListener(ISessionListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	public IMail createMail() {
		return new MailImpl();
	}

	public void sendMail(IAccount account, IMail mail) {
		
	}
}