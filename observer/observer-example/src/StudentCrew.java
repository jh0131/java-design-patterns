public class StudentCrew implements Crew {

    private String name;

    public StudentCrew(String name) {
        this.name = name;
    }

    @Override
    public void update(String msg) {

        System.out.println("msg = " + msg + "  name = " + name);
    }
}
