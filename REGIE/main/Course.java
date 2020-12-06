package REGIE.main;

public class Course {
    /*  Class that represents a section of course. 
        All the fields are set to be public for the ease of access. 
        Although it also makes it easy to modify the fields, it has no effect because the actual data in the backend database does not change. 
    */
    public String courseName;
    public String courseId;
    public String instrName;
    public String department;
    public String time;
    public String room;
    public int maxNum;
    public int regNum;
    public String description;

    Course(String name, String id, String instr, String depart, String t, String r, int maxN, int regN, String descrip) 
    {
        this.courseName = name;
        this.courseId = id;
        this.instrName = instr;
        this.department = depart;
        this.time = t;
        this.room = r;
        this.maxNum = maxN;
        this.regNum = regN;
        this.description = descrip;
    }

    /* Display the course info. */
    public void display_info() {
        System.out.printf("course name: %s, id: %s, instrutor: %s, time: %s, room: %s\ncurrent enrollment number: %d, maximum enrollment number: %d\ndescription: %s\n\n", 
                courseName, courseId, instrName, time, room, regNum, maxNum, description);
    }
}