package controllers;

import java.util.UUID;

import play.*;
import play.libs.Json;
import play.mvc.*;
import views.html.*;
import models.Track;
import models.TrackFactory;

public class Tracks extends Controller
{
	private static TrackFactory factory = new TrackFactory();
	
	public static Result index() {
	    return ok(tracks.render(factory.findSome()));
	}

	public static Result create() {
		Track newTrack = Json.fromJson(request().body().asJson(), Track.class);
	    newTrack.save();
		return ok(track.render(newTrack));
	}

	public static Result update(String id) {
		Track dbTrack = factory.findById(UUID.fromString(id));
		Track newTrack = Json.fromJson(request().body().asJson(), Track.class);
	    dbTrack.update(newTrack); // some model logic you would write to do a safe merge
	    dbTrack.save();
	    return show(id);
	}

	public static Result delete(String id) {
		factory.findById(UUID.fromString(id)).delete();
		return noContent();
	}

	public static Result show(String id)  {
		Track tr = factory.findById(UUID.fromString(id));
		return ok(track.render(tr));
	}
}
/*
public static Promise<Result> myAsyncAction() {
  Promise<Integer> promiseOfInt = Promise.promise(
    new Function0<Integer>() {
      public Integer apply() {
        return intensiveComputation();
      }
    }
  );
  return promiseOfInt.map(
    new Function<Integer, Result>() {
      public Result apply(Integer i) {
        return ok("Got result: " + i);
      }
    }
  );
}
*/
