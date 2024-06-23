package main;

import Views.Login;

public class main {
public static void main(String[] args) {
	new Thread(new Runnable() {
		
		@Override
		public void run() {
			Login login= new Login();
			login.initialize();
		}
	});
}
}
