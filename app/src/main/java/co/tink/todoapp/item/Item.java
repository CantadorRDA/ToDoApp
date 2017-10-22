package co.tink.todoapp.item;

/**
 * Created by Cantador on 22.10.17.
 */

public class Item {

  private String Title;
  private boolean Done;
  private long Date;

  public Item() {
  }

  public Item(
      String Title,
      boolean Done,
      long Date
  ) {
    this.Title = Title;
    this.Done = Done;
    this.Date = Date;
  }

  public void setTitle(String Title) {
    this.Title = Title;
  }

  public String getTitle() {
    return Title;
  }

  public void setDone(boolean Done) {
    this.Done = Done;
  }

  public boolean getDone() {
    return Done;
  }

  public void setDate(long Date) {
    this.Date = Date;
  }

  public long getDate() {
    return Date;
  }

}
