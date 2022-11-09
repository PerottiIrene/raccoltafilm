package it.prova.raccoltafilm.web.servlet.utente;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.prova.raccoltafilm.service.MyServiceFactory;
import it.prova.raccoltafilm.service.RuoloService;
import it.prova.raccoltafilm.service.UtenteService;

/**
 * Servlet implementation class PrepareInsertUtenteServlet
 */
@WebServlet("/PrepareInsertUtenteServlet")
public class PrepareInsertUtenteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private UtenteService utenteService;
	private RuoloService ruoloService;

	public PrepareInsertUtenteServlet() {
		this.utenteService = MyServiceFactory.getUtenteServiceInstance();
		this.ruoloService=MyServiceFactory.getRuoloServiceInstance();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// questo mi serve per la select di film in pagina
			request.setAttribute("ruoli_list_attribute", ruoloService.listAll());
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Attenzione si è verificato un errore.");
			request.getRequestDispatcher("home").forward(request, response);
			return;
		}

		request.getRequestDispatcher("/utente/insert.jsp").forward(request, response);
	}

}
