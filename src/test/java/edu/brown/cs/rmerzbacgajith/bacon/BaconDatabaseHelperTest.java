package edu.brown.cs.gajith.bacon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Ensures every public method in BaconDatabaseHelper is accurate.
 *
 * @author gokulajith
 *
 */
public class BaconDatabaseHelperTest {

  /**
   * Test GetActorID from actorName as well as GetActorName from ID.
   */
  @Test
  public void testActorPropertyQueryingCaching() {

    BaconCommand bc = new BaconCommand();

    // correct command where connection is created
    bc.mdbCommand("data/bacon/bacon.sqlite3");
    assertNotNull(bc.getdbHelper());

    BaconDatabaseHelper dbHelper = bc.getdbHelper();

    // If actor doesnt exist, return null for ID
    String fakeID = dbHelper.getActorID("NotARealActor");
    assertNull(fakeID);

    // Find actorID from name
    String actorID = dbHelper.getActorID("Bill Murray");
    assertEquals(actorID, "/m/0p_pd");

    // If actor doesnt exist, return null for Name
    String fakeName = dbHelper.getActorName("NotARealID");
    assertNull(fakeName);

    // Find actorName from ID
    String actorName = dbHelper.getActorName("/m/0p_pd");
    assertEquals(actorName, "Bill Murray");

  }

  /**
   * Test GetMovieName from Movieid.
   */
  @Test
  public void testMoviePropertyQueryingCaching() {

    BaconCommand bc = new BaconCommand();

    // correct command where connection is created
    bc.mdbCommand("data/bacon/bacon.sqlite3");
    assertNotNull(bc.getdbHelper());

    BaconDatabaseHelper dbHelper = bc.getdbHelper();

    // If movie doesnt exist, return null for name
    String fakeMovie = dbHelper.getMovieName("NotARealID");
    assertNull(fakeMovie);

    String realMovie = dbHelper.getMovieName("/m/011x_4");
    assertEquals(realMovie, "Groundhog Day");

  }

  /**
   * Test Getting all movies an actor has been in.
   */
  @Test
  public void testGetMoviesFromActor() {

    BaconCommand bc = new BaconCommand();

    // correct command where connection is created
    bc.mdbCommand("data/bacon/bacon.sqlite3");
    assertNotNull(bc.getdbHelper());

    BaconDatabaseHelper dbHelper = bc.getdbHelper();
    String actorId = dbHelper.getActorID("Taylor Swift");

    List<Movie> movies = dbHelper.getMoviesFromActor(actorId);
    assertEquals(movies.size(), 4);

    List<String> movieTitles = new ArrayList<String>();
    for (Movie movie : movies) {
      movieTitles.add(movie.getName());
    }

    assertTrue(movieTitles.contains("Hannah Montana: The Movie"));
    assertTrue(
        movieTitles.contains("Campfire Stories;Comes Around Gos Around"));
    assertTrue(movieTitles.contains("The Lorax"));
    assertTrue(movieTitles.contains("Valentine's Day"));

  }

  /**
   * Test Getting all actors a movie has.
   */
  @Test
  public void testGetActorsFromMovie() {

    BaconCommand bc = new BaconCommand();

    // correct command where connection is created
    bc.mdbCommand("data/bacon/bacon.sqlite3");
    assertNotNull(bc.getdbHelper());

    BaconDatabaseHelper dbHelper = bc.getdbHelper();

    // Test AmericanEast movie
    List<Actor> actors = dbHelper.getActorsFromMovie("/m/02vnnzj");
    assertEquals(actors.size(), 12);

    List<String> actorTitles = new ArrayList<String>();
    for (Actor actor : actors) {
      actorTitles.add(actor.getName());
    }

    // Make sure all actors in db are there
    assertTrue(actorTitles.contains("Tony Shalhoub"));
    assertTrue(actorTitles.contains("Tim Guinee"));
    assertTrue(actorTitles.contains("Christopher Carley"));
    assertTrue(actorTitles.contains("Erick Avari"));
    assertTrue(actorTitles.contains("Sarah Shahi"));
    assertTrue(actorTitles.contains("Sammy Sheik"));
    assertTrue(actorTitles.contains("Sayed Badreya"));
    assertTrue(actorTitles.contains("Anthony Azizi"));
    assertTrue(actorTitles.contains("Michael Bretten"));
    assertTrue(actorTitles.contains("Mike Batayeh"));
    assertTrue(actorTitles.contains("Tay Blessey"));

  }

}
