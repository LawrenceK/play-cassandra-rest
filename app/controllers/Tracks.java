package controllers;

import play.*;
import play.libs.Json;
import play.mvc.*;
import views.html.*;
import models.Track;

public class Tracks extends Controller{
	public static Result index() {
	    return ok(tracks.render("Search"));
	}

	public static Result create() {
		Track newTrack = Json.fromJson(request().body().asJson(), Track.class);
	    newTrack.save();
	    return show(newTrack.id);
	}

	public static Result update(Long id) {
		Track dbTrack = Track.findById(id);
		Track newTrack = Json.fromJson(request().body().asJson(), Track.class);
	    dbTrack.update(newTrack); // some model logic you would write to do a safe merge
	    dbTrack.save();
	    return show(id);
	}

	public static Result delete(Long id) {
		Track.findById(id).delete();
		return noContent();
	}

	public static Result show(Long id)  {
		Track tr = Track.findById(id);
		return ok(track.render(tr));
	}
}
