package Objects;
import java.time.LocalDate;

public interface Holiday {

    public LocalDate getDateInYear(int year);
    public String getName();
    public void setName(String name);

}
