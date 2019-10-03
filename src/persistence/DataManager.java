package persistence;

import classfiles.*;

import java.sql.*;
import java.util.*;

public class DataManager {
    private final String userName = "root";
    private final String password = "root";
    private final String serverName = "localhost";
    private final String portNumber = "3306";


    private Connection connection;

    // Il DataManager deve tener traccia di quali oggetti in memoria
    // corrispondono a quali record del DB. Per questo usa una doppia
    // mappa per ciascun tipo di oggetto caricato
    private Map<Task, Integer> taskObjects;
    private Map<Integer, Task> idToTaskObject;

    private Map<Event, Integer> eventObjects;
    private Map<Integer, Event> idToEventObject;

    private Map<Recipe, Integer> recipeObjects;
    private Map<Integer, Recipe> idToRecipeObject;

    private Map<Cook, Integer> cookObjects;
    private Map<Integer, Cook> idToCookObject;


    private Map<Section, Integer> sectionObjects;
    private Map<Integer, Section> idToSectionObject;

    private Map<MenuItem, Integer> itemObjects;
    private Map<Integer, MenuItem> idToItemObject;

    public DataManager() {

        this.eventObjects = new HashMap<>();
        this.idToEventObject = new HashMap<>();
        this.taskObjects = new HashMap<>();
        this.idToTaskObject = new HashMap<>();
        this.cookObjects = new HashMap<>();
        this.idToCookObject = new HashMap<>();
        this.recipeObjects = new HashMap<>();
        this.idToRecipeObject = new HashMap<>();


        this.sectionObjects = new HashMap<>();
        this.idToSectionObject = new HashMap<>();
        this.itemObjects = new HashMap<>();
        this.idToItemObject = new HashMap<>();
    }


    public void initialize() throws SQLException {
        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", this.userName);
        connectionProps.put("password", this.password);
        connectionProps.put("useUnicode", true);
        connectionProps.put("useJDBCCompliantTimezoneShift", true);
        connectionProps.put("useLegacyDatetimeCode", false);
        connectionProps.put("serverTimezone", "UTC");

        conn = DriverManager.getConnection(
                "jdbc:mysql://" +
                        this.serverName +
                        ":" + this.portNumber + "/catering",
                connectionProps);

        System.out.println("Connected to database");
        this.connection = conn;

    }

    /**
     * @return all the events of the database
     */
    public List<Event> loadEvents() {
        Statement st = null;
        String query = "SELECT * FROM Events";
        List<Event> ret = new ArrayList<>();

        try {
            st = this.connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                String name = rs.getString("name");
                int id = rs.getInt("id");

                // Verifica se per caso l'ha già caricata
                Event event = this.idToEventObject.get(id);

                if (event == null) {
                    event = new Event(name);

                    ret.add(event);
                    this.eventObjects.put(event, id);
                    this.idToEventObject.put(id, event);
                }
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            try {
                if (st != null) st.close();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * Carica tutti i cuochi del database
     * @return List<Cook>
     */
    public List<Cook> loadCooks() {
        Statement st = null;
        String query ="SELECT * FROM Cooks";
        List<Cook> ret = new ArrayList<>();

        try {
            st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                String name = rs.getString("name");
                int id = rs.getInt("id");

                //Verifico se è già stato caricato
                Cook cook = idToCookObject.get(id);

                if (cook == null) {
                    cook = new Cook(name);

                    if (cook != null) {
                        ret.add(cook);
                        this.cookObjects.put(cook, id);
                        this.idToCookObject.put(id, cook);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null)
                    st.close();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * Carica tutti i task di un evento
     * @param event è l'evento da cui prendere i task
     * @return ritorna una lista di task dell'evento
     */
    public List<Task> loadTasks(Event event) {
        Statement st = null;
        String query = "SELECT Tasks.id, Recipes.name as \"ricetta\", C.name, start_time, end_time from Tasks inner join Events E on Tasks.evento = E.id inner join Recipes on Tasks.ricetta = Recipes.id left outer join Cooks C on Tasks.cuoco = C.id where E.name=\'" + event.getName() + "\'";
        PreparedStatement preparedStatement = null;
        List<Task> ret = new ArrayList<>();

        try {
            preparedStatement = connection.prepareStatement(query);
//            preparedStatement.setString(1, event.getName());
            ResultSet rs = preparedStatement.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt(1);
                String ricetta = rs.getString(2);
                String cuoco = rs.getString(3);
                System.out.println(cuoco);
                String startTime = rs.getString(4);
                String endTime = rs.getString(5);
//                System.out.println("\n\n" + event.getName());
//                System.out.println("Ricetta: " + ricetta);
//                System.out.println("Cuoco: " + cuoco);
//                System.out.println("Inizio del turno: " + startTime);
//                System.out.println("Fine del turno: " + endTime);

                // Verifica se per caso l'ha già caricata
                Task task = this.idToTaskObject.get(id);

                if (task == null) {

                    if (cuoco == null) task = new Task(new Recipe(ricetta), startTime, endTime);
                    else task = new Task(new Recipe(ricetta), new Cook(cuoco), startTime, endTime);

                    ret.add(task);
                    this.taskObjects.put(task, id);
                    this.idToTaskObject.put(id, task);
                }
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            try {
                if (st != null) st.close();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * Carica tutte le ricette del database
     * @return List<Recipe>
     */
    public List<Recipe> loadRecipes() {
        Statement st = null;
        String query = "SELECT * FROM Recipes";
        List<Recipe> ret = new ArrayList<>();

        try {
            st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                String name = rs.getString("name");
                int id = rs.getInt("id");

                // Verifica se per caso l'ha già caricata
                Recipe rec = this.idToRecipeObject.get(id);

                if (rec == null) {
                    rec = new Recipe(name);

                    ret.add(rec);
                    this.recipeObjects.put(rec, id);
                    this.idToRecipeObject.put(id, rec);
                }
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            try {
                if (st != null) st.close();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }
        }
        return ret;
    }

    /**
     *
     * @param cook
     * @param task
     */
    public void bindCookToTask(Cook cook, Task task) {
        System.out.println("\n");
        int tId = taskObjects.get(task);
        int cId = cookObjects.get(cook);
        System.out.println(tId);
        String sql = "UPDATE Tasks SET cuoco = ? where id = ?";
        PreparedStatement pstmt = null;

        try {
            pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, cId);
            pstmt.setInt(2, tId);

            pstmt.executeUpdate();

        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }
        }
    }
    public void removeTask(Task t){
        int tId = taskObjects.get(t);
        String query = "DELETE FROM Tasks WHERE id=?";
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, tId);

            pstmt.executeUpdate();

        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }
        }
    }
    public void addTask(Task t, Event e){
        Cook tempC = t.getCook();
        Recipe tempR = t.getRecipe();
        int eId = eventObjects.get(e);
        int rId = recipeObjects.get(tempR);

        String query = "INSERT INTO Tasks (ricetta,evento) VALUES (?, ?)";
        PreparedStatement pstmt = null;
        String querona = "SELECT MAX(id) as id FROM Tasks";
        Statement pstmt2 = null;
        try {
            pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, rId);
         //   pstmt.setInt(2, cId);
            pstmt.setInt(2, eId);
          //  pstmt.setTime(3, Time.valueOf(t.getStartTime()));
          //  pstmt.setTime(4, Time.valueOf(t.getEndTime()));
          //  pstmt.setTime(5, Time.valueOf(t.getEstimatedTime()));


            pstmt.executeUpdate();
            pstmt2 = connection.createStatement();
            ResultSet rs = pstmt2.executeQuery(querona);
            System.out.println("BARABBA BABBEO" + 1);
            if(rs.next()){
                int newId = rs.getInt("id");
            }

         //   taskObjects.put(t);o


        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (pstmt2 != null) pstmt2.close();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }
        }
    }
}
