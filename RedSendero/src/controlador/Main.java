package controlador;
import java.awt.EventQueue;

public class Main {
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Controlador controlador = new Controlador();
					controlador.abrirInterfaz();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}