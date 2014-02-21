package controllers;

import java.util.List;
import java.util.UUID;

import play.*;
import play.libs.Json;
import play.mvc.*;
import play.libs.F.*;
import java.util.concurrent.Callable;
import views.html.*;
import models.Track;
import models.TrackFactory;
import static play.libs.F.Promise.promise;

public class Tracks extends Controller
{
	private static TrackFactory factory = new TrackFactory();
	
	public static Result index() {
//	    return ok(tracks.render(factory.findSome()));
        return async(
                promise(new Function0<List<Track>>() {
                    public List<Track> apply() {
                        return factory.findSome();
                    }
                }).map(new Function<List<Track>,Result>() {
                    public Result apply(List<Track> t) {
                    return ok(tracks.render(t));
                    }
                })
            );	}

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
