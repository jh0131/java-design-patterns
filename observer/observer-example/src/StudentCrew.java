public class StudentCrew implements Crew {

    // setter 따로 없어서 final 선언
    private final String name;

    public StudentCrew(String name) {
        this.name = name;
    }

    @Override
    public void update(String msg) {

        System.out.println("msg = " + msg + "  name = " + name);
    }
}
