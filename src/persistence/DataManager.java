package persistence;

import classfiles.*;

import java.sql.*;
import java.util.*;

public class DataManager {
    private final String userName;
    private final String password;
    private final String serverName;
    private final String portNumber;


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


    public DataManager() {
        serverName = "localhost";
        portNumber = "3306";
        password = "root";
        userName = "root";

        this.eventObjects = new HashMap<>();
        this.idToEventObject = new HashMap<>();
        this.taskObjects = new HashMap<>();
        this.idToTaskObject = new HashMap<>();
        this.cookObjects = new HashMap<>();
        this.idToCookObject = new HashMap<>();
        this.recipeObjects = new HashMap<>();
        this.idToRecipeObject = new HashMap<>();

    }


    public void initialize() throws SQLException {
        Connection conn;
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

                    ret.add(cook);
                    this.cookObjects.put(cook, id);
                    this.idToCookObject.put(id, cook);

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

    public List<CookAvailability> loadCookAvailability(Cook cook) {
        Statement st = null;
        String query = "SELECT day, DATE_FORMAT(start, \"%H:%i\"), DATE_FORMAT(end, \"%H:%i\") from Cooks join cooks_availability ca on Cooks.id = ca.cook_id where name=\'" + cook.getName() + "\'";
        PreparedStatement preparedStatement;
        List<CookAvailability> ret = new ArrayList<>();

        try {
            preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery(query);
            while (rs.next()) {
                String day = rs.getString(1);
                String start = rs.getString(2);
                String end = rs.getString(3);

                CookAvailability shift = new CookAvailability(day, start, end);
                ret.add(shift);
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
     * Carica tutti i task di un evento
     * @param event è l'evento da cui prendere i task
     * @return ritorna una lista di task dell'evento
     */
    public List<Task> loadTasks(Event event) {
        Statement st = null;
        String query = "SELECT Tasks.id, Recipes.name as \"ricetta\", C.name, DATE_FORMAT(start_time, \"%H:%i\"), DATE_FORMAT(end_time, \"%H:%i\"), DATE_FORMAT(estimated_time, \"%H:%i\"), doses, already_prepared_doses from Tasks inner join Events E on Tasks.evento = E.id inner join Recipes on Tasks.ricetta = Recipes.id left outer join Cooks C on Tasks.cuoco = C.id where E.name=\'" + event.getName() + "\'";
        PreparedStatement preparedStatement;
        List<Task> ret = new ArrayList<>();

        try {
            preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt(1);
                String ricetta = rs.getString(2);
                String cuoco = rs.getString(3);
//                    System.out.println(cuoco);
                String startTime = rs.getString(4);
                String endTime = rs.getString(5);
                String estimatedTime = rs.getString(6);
                int doses = rs.getInt(7);
                int alreadyPreparedDoses = rs.getInt(8);

                // Verifica se per caso l'ha già caricata
                Task task = this.idToTaskObject.get(id);

                if (task == null) {

                    if (cuoco == null) {
                        task = new Task(new Recipe(ricetta), null, startTime, endTime, estimatedTime, doses, alreadyPreparedDoses);
                    } else {
                        task = new Task(new Recipe(ricetta), new Cook(cuoco), startTime, endTime, estimatedTime, doses, alreadyPreparedDoses);
                    }


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
     * @param cook cuoco a cui assegnare il task
     * @param task il task da assegnare al cuoco
     */
    public void bindCookToTask(Cook cook, Task task) {
        int tId = taskObjects.get(task);
        int cId = cookObjects.get(cook);
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

        String query = "INSERT INTO Tasks (ricetta, evento) VALUES (?, ?)";
        PreparedStatement pstmt = null;
        String querona = "SELECT MAX(id) as id FROM Tasks";
        Statement pstmt2 = null;
        try {
            pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, rId);
            pstmt.setInt(2, eId);

            pstmt.executeUpdate();
            pstmt2 = connection.createStatement();
            ResultSet rs = pstmt2.executeQuery(querona);
            if(rs.next()){
                int newId = rs.getInt("id");
                taskObjects.put(t, newId);
            }

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

    public void bindTimeToTask(Task task) {
        int tId = taskObjects.get(task);
        String sTime = task.getStartTime();
        String eTime = task.getEndTime();
        String estTime = task.getEstimatedTime();
        int doses = task.getDoses();
        int alreadyPreparedDoses = Integer.valueOf(task.getPreparedDoses());

        String query = "UPDATE Tasks SET start_time = ?, end_time = ?, estimated_time = ?, doses = ?, already_prepared_doses = ? WHERE id = ?";
        PreparedStatement pstmt = null;

        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, sTime);
            pstmt.setString(2, eTime);
            pstmt.setString(3, estTime);
            pstmt.setInt(4, doses);
            pstmt.setInt(5, alreadyPreparedDoses);
            pstmt.setInt(6, tId);
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
}
