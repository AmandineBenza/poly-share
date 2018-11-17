package com.lama.polyshare.scores;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.EntityQuery;
import com.google.cloud.datastore.QueryResults;
import com.lama.polyshare.datastore.model.UserManager;

import com.google.cloud.datastore.EntityQuery.Builder;

import com.google.cloud.datastore.StructuredQuery.OrderBy;

public class ServletLeaderBoard extends HttpServlet {

	private static final long serialVersionUID = 5236901201231220762L;
	private final static Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		resp.getWriter().print("LeaderBoard !\n");
		rankUsers(5, resp.getWriter());
	};

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

	};

	public void rankUsers(int nbUsers, PrintWriter writer) {
		Builder users = UserManager.instance.getUserQueryBuilder(nbUsers);
		users.setOrderBy(OrderBy.asc("points"));

		EntityQuery query = users.build();
		QueryResults<Entity> entities = datastore.run(query);

		int i = 1;

		while (entities.hasNext()) {
			Entity user = entities.next();
			String mail = user.getString("mail");
			String rank = user.getString("rank");
			int points = (int) user.getLong("points");

			writer.print(i + ". " + mail + " | " + rank + " | " + points + " \n");
			i++;
		}
	}

}