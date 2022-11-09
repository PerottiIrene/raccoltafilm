package it.prova.raccoltafilm.web.servlet.utente;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.prova.raccoltafilm.dao.RuoloDAO;
import it.prova.raccoltafilm.model.Ruolo;
import it.prova.raccoltafilm.model.StatoUtente;
import it.prova.raccoltafilm.model.Utente;
import it.prova.raccoltafilm.service.MyServiceFactory;
import it.prova.raccoltafilm.service.RuoloService;
import it.prova.raccoltafilm.service.UtenteService;
import it.prova.raccoltafilm.utility.UtilityForm;

/**
 * Servlet implementation class ExecuteInsertUtenteServlet
 */
@WebServlet("/ExecuteInsertUtenteServlet")
public class ExecuteInsertUtenteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private UtenteService utenteService;
	private RuoloService ruoloService;

	public ExecuteInsertUtenteServlet() {
		this.utenteService = MyServiceFactory.getUtenteServiceInstance();
		this.ruoloService=MyServiceFactory.getRuoloServiceInstance();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// estraggo input
		String usernameParam = request.getParameter("username");
		String passwordParam = request.getParameter("password");
		String passwordConfermaParam = request.getParameter("passwordConferma");
		Date dataCreazioneParam = new Date();
		String nomeParam = request.getParameter("nome");
		String cognomeParam = request.getParameter("cognome");
		String [] ruoliParam=request.getParameterValues("ruoli");
		
		HashSet<Ruolo> listaRuoli=new HashSet<>();

		// preparo un bean (che mi serve sia per tornare in pagina
		// che per inserire) e faccio il binding dei parametri
		Utente utenteInstance = new Utente(usernameParam, passwordParam, cognomeParam, nomeParam,
				dataCreazioneParam);
		
		utenteInstance.setStato(StatoUtente.CREATO);
		
		try {		
			// se la validazione non risulta ok
			if (!UtilityForm.validateUtenteBean(utenteInstance)) {
				request.setAttribute("insert_utente_attr", utenteInstance);
				// questo mi serve per la select di registi in pagina
				request.setAttribute("ruoli_list_attribute", ruoloService.listAll());
				request.setAttribute("errorMessage", "Attenzione sono presenti errori di validazione");
				request.getRequestDispatcher("/utente/insert.jsp").forward(request, response);
				return;
			}

			if(!passwordParam.equals(passwordConfermaParam)) {
				request.setAttribute("insert_utente_attr", utenteInstance);
			// questo mi serve per la select di registi in pagina
			request.setAttribute("ruoli_list_attribute", ruoloService.listAll());
			request.setAttribute("errorMessage", "Attenzione le due password non coincidono");
			request.getRequestDispatcher("/utente/insert.jsp").forward(request, response);
			return;
			}
			
			Ruolo ruoloInstance=new Ruolo();
			for(String element:ruoliParam) {
				ruoloInstance=ruoloService.caricaSingoloElemento(Long.parseLong(element));
				listaRuoli.add(ruoloInstance);
			}
			utenteInstance.setRuoli(listaRuoli);
			// se sono qui i valori sono ok quindi posso creare l'oggetto da inserire
			// occupiamoci delle operazioni di business
			utenteService.inserisciNuovo(utenteInstance);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Attenzione si è verificato un errore.");
			request.getRequestDispatcher("/utente/insert.jsp").forward(request, response);
			return;
		}

		// andiamo ai risultati
		// uso il sendRedirect con parametro per evitare il problema del double save on
		// refresh
		response.sendRedirect("ExecuteListUtenteServlet?operationResult=SUCCESS");
	}

}
