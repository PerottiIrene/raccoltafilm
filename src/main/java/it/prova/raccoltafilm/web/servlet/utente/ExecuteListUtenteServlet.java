package it.prova.raccoltafilm.web.servlet.utente;


import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import it.prova.raccoltafilm.service.MyServiceFactory;
import it.prova.raccoltafilm.service.UtenteService;

/**
 * Servlet implementation class ExecuteListUtenteServlet
 */
@WebServlet("/ExecuteListUtenteServlet")
public class ExecuteListUtenteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// injection del Service
	private UtenteService utenteService;

	public ExecuteListUtenteServlet() {
		this.utenteService = MyServiceFactory.getUtenteServiceInstance();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// se nell'url della request è presente SUCCESS significa che devo mandare un
			// messaggio di avvenuta operazione in pagina
			String operationResult = request.getParameter("operationResult");
			if (StringUtils.isNotBlank(operationResult) && operationResult.equalsIgnoreCase("SUCCESS"))
				request.setAttribute("successMessage", "Operazione effettuata con successo");
			if (StringUtils.isNotBlank(operationResult) && operationResult.equalsIgnoreCase("ERROR"))
				request.setAttribute("errorMessage", "Attenzione si è verificato un errore.");
			if (StringUtils.isNotBlank(operationResult) && operationResult.equalsIgnoreCase("NOT_FOUND"))
				request.setAttribute("errorMessage", "Elemento non trovato.");

			request.setAttribute("utente_list_attribute", utenteService.listAll());
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Attenzione si è verificato un errore.");
			request.getRequestDispatcher("home").forward(request, response);
			return;
		}

		// andiamo ai risultati
		request.getRequestDispatcher("/utente/list.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
