package it.prova.raccoltafilm.web.servlet.utente;


import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;

import it.prova.raccoltafilm.model.Utente;
import it.prova.raccoltafilm.service.MyServiceFactory;
import it.prova.raccoltafilm.service.UtenteService;

/**
 * Servlet implementation class ExecuteVisualizzaUtenteServlet
 */
@WebServlet("/ExecuteVisualizzaUtenteServlet")
public class ExecuteVisualizzaUtenteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// injection del Service
	private UtenteService utenteService;

	public ExecuteVisualizzaUtenteServlet() {
		this.utenteService = MyServiceFactory.getUtenteServiceInstance();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String idUtenteParam = request.getParameter("idUtente");

		if (!NumberUtils.isCreatable(idUtenteParam)) {
			// qui ci andrebbe un messaggio nei file di log costruito ad hoc se fosse attivo
			request.setAttribute("errorMessage", "Attenzione si è verificato un errore.id");
			request.getRequestDispatcher("home").forward(request, response);
			return;
		}

		try {
			Utente utenteInstance = utenteService.caricaSingoloElementoEager(Long.parseLong(idUtenteParam));

			if (utenteInstance == null) {
				request.setAttribute("errorMessage", "Elemento non trovato.");
				request.getRequestDispatcher("ExecuteListUtenteServlet?operationResult=NOT_FOUND").forward(request,
						response);
				return;
			}

			request.setAttribute("show_utente_attr", utenteInstance);
		} catch (Exception e) {
			// qui ci andrebbe un messaggio nei file di log costruito ad hoc se fosse attivo
			e.printStackTrace();
			request.setAttribute("errorMessage", "Attenzione si è verificato un errore.");
			request.getRequestDispatcher("home").forward(request, response);
			return;
		}

		request.getRequestDispatcher("/utente/show.jsp").forward(request, response);

	}

}
