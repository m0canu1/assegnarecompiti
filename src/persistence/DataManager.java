package persistence;

import classfiles.*;

import java.sql.*;
import java.util.*;

public class DataManager {
    private String userName = "root";
    private String password = "root";
    private String serverName = "localhost";
    private String portNumber = "3306";


    private Connection connection;

    // Il DataManager deve tener traccia di quali oggetti in memoria
    // corrispondono a quali record del DB. Per questo usa una doppia
    // mappa per ciascun tipo di oggetto caricato
    private Map<User, Integer> userObjects;
    private Map<Integer, User> idToUserObject;

    private Map<Recipe, Integer> recipeObjects;
    private Map<Integer, Recipe> idToRecipeObject;

    private Map<Menu, Integer> menuObjects;
    private Map<Integer, Menu> idToMenuObject;

    private Map<Section, Integer> sectionObjects;
    private Map<Integer, Section> idToSectionObject;

    private Map<MenuItem, Integer> itemObjects;
    private Map<Integer, MenuItem> idToItemObject;

    public DataManager() {

        this.userObjects = new HashMap<>();
        this.idToUserObject = new HashMap<>();
        this.recipeObjects = new HashMap<>();
        this.idToRecipeObject = new HashMap<>();
        this.menuObjects = new HashMap<>();
        this.idToMenuObject = new HashMap<>();
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


        CateringAppManager.menuManager.addReceiver(new MenuEventReceiver() {
            @Override
            public void notifyMenuCreated(Menu m) {
                int mid = writeNewMenu(m);
                menuObjects.put(m, mid);
                idToMenuObject.put(mid, m);
                List<Section> secs = m.getSections();
                for (int i = 0; i < secs.size(); i++) {
                    Section s = secs.get(i);
                    int sid = writeNewSection(mid, i, s);
                    sectionObjects.put(s, sid);
                    idToSectionObject.put(sid, s);

                    List<MenuItem> secItems = s.getItems();

                    for (int j = 0; j < secItems.size(); j++) {
                        MenuItem it = secItems.get(j);
                        int iid = writeNewItem(mid, sid, j, it);
                        itemObjects.put(it, iid);
                        idToItemObject.put(iid, it);
                    }
                }

                List<MenuItem> menuItems = m.getItemsWithoutSection();
                for (int z=0; z < menuItems.size(); z++) {
                    MenuItem it = menuItems.get(z);
                    int iid = writeNewItem(mid, 0, z, it);
                    itemObjects.put(it, iid);
                    idToItemObject.put(iid, it);
                }
            }

            @Override
            public void notifySectionAdded(Menu m, Section s) {
                int mid = menuObjects.get(m);
                int pos = m.getSectionPosition(s);
                int sid = writeNewSection(mid, pos, s);
                sectionObjects.put(s, sid);
                idToSectionObject.put(sid, s);
            }

            @Override
            public void notifyItemAdded(Menu m, Section s, MenuItem it) {
                int mid = menuObjects.get(m);
                int sid, pos;
                if (s != null) {
                    sid = sectionObjects.get(s);
                    pos = s.getItemPosition(it);
                } else {
                    sid = 0;
                    pos = m.getItemPosition(it);
                }
                int iid = writeNewItem(mid, sid, pos, it);
                itemObjects.put(it, iid);
                idToItemObject.put(iid, it);
            }

            @Override
            public void notifyMenuPublished(Menu m) {
                writeMenuChanges(m);

            }

            @Override
            public void notifyMenuDeleted(Menu m) {
                removeMenu(m);
            }

            @Override
            public void notifySectionRemoved(Menu m, Section s) {
                removeSection(s);
            }


            @Override
            public void notifySectionNameChanged(Menu m, Section s) {
                int mid = menuObjects.get(m);
                int pos = m.getSectionPosition(s);
                writeSectionChanges(mid, pos, s);
            }

            @Override
            public void notifySectionsRearranged(Menu m) {
                List<Section> sects = m.getSections();
                int mid = menuObjects.get(m);
                for (int i = 0; i < sects.size(); i++) {
                    writeSectionChanges(mid, i, sects.get(i));
                }

            }

            @Override
            public void notifyItemsRearranged(Menu m, Section s) {
                List<MenuItem> its = s.getItems();
                int mid = menuObjects.get(m);
                int sid = sectionObjects.get(s);
                for (int i = 0; i < its.size(); i++) {
                    writeItemChanges(mid, sid, i, its.get(i));
                }
            }

            @Override
            public void notifyItemsRearrangedInMenu(Menu m) {
                List<MenuItem> its = m.getItemsWithoutSection();
                int mid = menuObjects.get(m);
                for (int i = 0; i < its.size(); i++) {
                    writeItemChanges(mid, 0, i, its.get(i));
                }

            }

            @Override
            public void notifyItemMoved(Menu m, Section oldS, Section newS, MenuItem it) {
                int mid = menuObjects.get(m);
                int sid = (newS == null ? 0 : sectionObjects.get(newS));
                int itpos = (newS == null ? m.getItemPosition(it) : newS.getItemPosition(it));
                writeItemChanges(mid, sid, itpos, it);
            }

            @Override
            public void notifyItemDescriptionChanged(Menu m, MenuItem it) {
                int mid = menuObjects.get(m);
                Section s = m.getSection(it);
                int sid = (s == null ? 0 : sectionObjects.get(s));
                int itpos = (s == null ? m.getItemPosition(it) : s.getItemPosition(it));
                writeItemChanges(mid, sid, itpos, it);
            }

            @Override
            public void notifyItemDeleted(Menu m, MenuItem it) {
                removeItem(it);
            }

            @Override
            public void notifyMenuTitleChanged(Menu m) {
                writeMenuChanges(m);
            }
        });
    }

    private int writeNewMenu(Menu m) {

        String sql = "INSERT INTO Menus(title, menuowner, published, fingerFood, " +
                "cookRequired, hotDishes, kitchenRequired, buffet) "
                + "VALUES(?,?,?,?,?,?,?,?)";
        int id = -1;
        PreparedStatement pstmt = null;
        try {
            pstmt = this.connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, m.getTitle());
            pstmt.setInt(2, this.userObjects.get(m.getOwner()));
            pstmt.setBoolean(3, m.isPublished());
            pstmt.setBoolean(4, m.isFingerFood());
            pstmt.setBoolean(5, m.isCookRequired());
            pstmt.setBoolean(6, m.isHotDishes());
            pstmt.setBoolean(7, m.isKitchenRequired());
            pstmt.setBoolean(8, m.isBuffet());

            int r = pstmt.executeUpdate();

            if (r == 1) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    id = rs.getInt(1);
                }
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }
        }
        return id;
    }

    private void writeMenuChanges(Menu m) {
        int mid = menuObjects.get(m);
        int uid = userObjects.get(m.getOwner());
        String sql = "UPDATE Menus SET menuowner=?, published=?, fingerFood=?, cookRequired=?, hotDishes=?, "
                + "kitchenRequired=?, buffet=?, title=? WHERE id=" + mid;
        PreparedStatement pstmt = null;

        try {
            pstmt = this.connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, uid);
            pstmt.setBoolean(2, m.isPublished());
            pstmt.setBoolean(3, m.isFingerFood());
            pstmt.setBoolean(4, m.isCookRequired());
            pstmt.setBoolean(5, m.isHotDishes());
            pstmt.setBoolean(6, m.isKitchenRequired());
            pstmt.setBoolean(7, m.isBuffet());
            pstmt.setString(8, m.getTitle());

            int r = pstmt.executeUpdate();
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

    private void removeMenu(Menu m) {
        int mId = menuObjects.get(m);
        String sqlItems = "DELETE FROM MenuItems WHERE menu=?";
        String sqlSections = "DELETE FROM Sections WHERE menu=?";
        String sqlMenu = "DELETE FROM Menus WHERE id=?";
        PreparedStatement pstItems = null;
        PreparedStatement pstSections = null;
        PreparedStatement pstMenu = null;
        try {
            connection.setAutoCommit(false);
            pstItems = connection.prepareStatement(sqlItems);
            pstSections = connection.prepareStatement(sqlSections);
            pstMenu = connection.prepareStatement(sqlMenu);
            pstItems.setInt(1, mId);
            pstSections.setInt(1, mId);
            pstMenu.setInt(1, mId);
            pstItems.executeUpdate();
            pstSections.executeUpdate();
            pstMenu.executeUpdate();
            connection.commit();
        } catch (SQLException exc) {
            exc.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
                if (pstItems != null) pstItems.close();
                if (pstSections != null) pstSections.close();
                if (pstMenu != null) pstMenu.close();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }

        }
    }


    private int writeNewSection(int menuId, int position, Section sec) {

        String sql = "INSERT INTO Sections(menu, name, position) VALUES(?,?,?)";
        int id = -1;
        PreparedStatement pstmt = null;
        try {
            pstmt = this.connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, menuId);
            pstmt.setString(2, sec.getName());
            pstmt.setInt(3, position);

            int r = pstmt.executeUpdate();

            if (r == 1) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    id = rs.getInt(1);
                }
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }
        }

        return id;
    }

    private void writeSectionChanges(int menuId, int position, Section sec) {
        int sId = sectionObjects.get(sec);
        String sql = "UPDATE Sections SET menu=?, name=?, position=? WHERE id=" + sId;
        PreparedStatement pstmt = null;
        try {
            pstmt = this.connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, menuId);
            pstmt.setString(2, sec.getName());
            pstmt.setInt(3, position);

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

    private void removeSection(Section s) {
        int sId = sectionObjects.get(s);
        String sqlItems = "DELETE FROM MenuItems WHERE section=?";
        String sqlSection = "DELETE FROM Sections WHERE id=?";
        PreparedStatement pstItems = null;
        PreparedStatement pstSection = null;
        try {
            connection.setAutoCommit(false);
            pstItems = connection.prepareStatement(sqlItems);
            pstSection = connection.prepareStatement(sqlSection);
            pstItems.setInt(1, sId);
            pstSection.setInt(1, sId);
            pstItems.executeUpdate();
            pstSection.executeUpdate();
            connection.commit();
        } catch (SQLException exc) {
            exc.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
                if (pstItems != null) pstItems.close();
                if (pstSection != null) pstSection.close();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }

        }
    }

    private int writeNewItem(int menuId, int secId, int position, MenuItem item) {

        String sql = "INSERT INTO MenuItems(menu, section, description, recipe, position) " +
                "VALUES(?,?,?,?,?)";
        int id = -1;
        PreparedStatement pstmt = null;

        try {
            pstmt = this.connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, menuId);
            pstmt.setInt(2, secId);
            pstmt.setString(3, item.getDescription());
            pstmt.setInt(4, recipeObjects.get(item.getRecipe()));
            pstmt.setInt(5, position);

            int r = pstmt.executeUpdate();

            if (r == 1) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    id = rs.getInt(1);
                }
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }
        }

        return id;
    }

    private void writeItemChanges(int menuId, int secId, int position, MenuItem item) {
        int itId = itemObjects.get(item);
        String sql = "UPDATE MenuItems SET menu=?, section=?, description=?, recipe=?, position=? WHERE " +
                "id=" + itId;
        PreparedStatement pstmt = null;

        try {
            pstmt = this.connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, menuId);
            pstmt.setInt(2, secId);
            pstmt.setString(3, item.getDescription());
            pstmt.setInt(4, recipeObjects.get(item.getRecipe()));
            pstmt.setInt(5, position);

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

    private void removeItem(MenuItem it) {
        int iId = itemObjects.get(it);
        String sqlItems = "DELETE FROM MenuItems WHERE id=?";
        PreparedStatement pstItems = null;
        try {
            pstItems = connection.prepareStatement(sqlItems);
            pstItems.setInt(1, iId);
            pstItems.executeUpdate();
        } catch (SQLException exc) {
            exc.printStackTrace();

        } finally {
            try {
                if (pstItems != null) pstItems.close();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }

        }
    }

    public User loadUser(String userName) {
        PreparedStatement pst = null;
        String sql = "SELECT Users.id, Users.name, UserRoles.role FROM Users LEFT JOIN UserRoles on Users.id = "
                + "UserRoles.user where Users.name=?";
        User u = null;

        try {
            pst = this.connection.prepareStatement(sql);
            pst.setString(1, userName);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                if (u == null) {
                    u = new User(userName);
                    int id = rs.getInt("id");
                    this.userObjects.put(u, id);
                    this.idToUserObject.put(id, u);
                }

                addUserRole(u, rs);

            }
            pst.close();
        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            try {
                if (pst != null) pst.close();
            } catch (SQLException exc2) {
                exc2.printStackTrace();
            }
        }
        return u;
    }

    public List<Recipe> loadRecipes() {
        Statement st = null;
        String query = "SELECT * FROM Recipes";
        List<Recipe> ret = new ArrayList<>();

        try {
            st = this.connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                String name = rs.getString("name");
                char type = rs.getString("type").charAt(0);
                int id = rs.getInt("id");

                // Verifica se per caso l'ha già caricata
                Recipe rec = this.idToRecipeObject.get(id);

                if (rec == null) {
                    rec = createRecipeWithType(name, type);

                    if (rec != null) {
                        ret.add(rec);
                        this.recipeObjects.put(rec, id);
                        this.idToRecipeObject.put(id, rec);
                    }
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

    public List<Menu> loadMenus() {
        List<Menu> ret = new ArrayList<>();
        Statement st = null;
        String query = "SELECT * FROM Menus";

        try {
            st = this.connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt("id");

                // Verifica se per caso l'ha già caricato
                Menu m = this.idToMenuObject.get(id);
                if (m == null) {

                    String title = rs.getString("title");

                    int ownerid = rs.getInt("menuowner");
                    User owner = this.innerLoadUser(ownerid);

                    m = new Menu(owner, title);
                    m.setPublished(rs.getBoolean("published"));
                    m.setBuffet(rs.getBoolean("buffet"));
                    m.setCookRequired(rs.getBoolean("cookRequired"));
                    m.setFingerFood(rs.getBoolean("fingerFood"));
                    m.setHotDishes(rs.getBoolean("hotDishes"));
                    m.setKitchenRequired(rs.getBoolean("kitchenRequired"));

                    // per sapere se il menu è in uso consulto la tabella degli eventi
                    // NdR: un menu è in uso anche se l'evento che lo usa è concluso o annullato
                    Statement st2 = this.connection.createStatement();
                    String query2 = "SELECT Events.id FROM Events JOIN Menus M on Events.menu = M.id WHERE M.id=" + id;
                    ResultSet rs2 = st2.executeQuery(query2);
                    m.setInUse(rs2.next());
                    st2.close();
                    loadMenuSections(id, m);
                    loadMenuItems(id, m);


                    ret.add(m);
                    this.menuObjects.put(m, id);
                    this.idToMenuObject.put(id, m);
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

    private void loadMenuItems(int id, Menu m) {
        // Caricamento voci
        // Non verifichiamo se un MenuItem è già stato creato perché
        // questo può avvenire solo nel contesto del caricamento di un Menu
        // e il MenuItem può essere già creato solo se il Menu è stato creato;
        // il controllo sul Menu avviene già in loadMenus
        Statement st = null;
        String query = "SELECT MenuItems.* FROM MenuItems WHERE MenuItems.menu=" + id
                + " ORDER BY MenuItems.position";
        try {
            st = this.connection.createStatement();

            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                String description = rs.getString("description");
                int idSec = rs.getInt("section");
                int idIt = rs.getInt("id");
                int idRec = rs.getInt("recipe");

                Recipe rec = this.innerLoadRecipe(idRec);

                Section sec = null;
                if (idSec > 0) {
                    // la sezione a questo punto dovrebbe essere già stata aggiunta
                    sec = this.idToSectionObject.get(idSec);
                }
                MenuItem it = m.addItem(rec, sec, description);
                this.itemObjects.put(it, idIt);
                this.idToItemObject.put(idIt, it);
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
    }

    private Recipe innerLoadRecipe(int idRec) {
        // verifico se l'ho già caricato in precedenza
        Recipe rec = this.idToRecipeObject.get(idRec);
        if (rec != null) return rec;

        Statement st = null;

        String query = "SELECT * FROM Recipes WHERE Recipes.id = " + idRec;
        try {
            st = this.connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                String name = rs.getString("name");
                char type = rs.getString("type").charAt(0);
                rec = createRecipeWithType(name, type);
                this.recipeObjects.put(rec, idRec);
                this.idToRecipeObject.put(idRec, rec);
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

        return rec;
    }

    private Recipe createRecipeWithType(String name, char type) {
        switch (type) {
            case 'r':
                return new Recipe(name, Recipe.Type.Dish);
            case 'p':
                return new Recipe(name, Recipe.Type.Preparation);

        }
        return null;
    }

    private void loadMenuSections(int id, Menu m) {
        // Caricamento sezioni
        // Non verifichiamo se una Section è già stata creata perché
        // questo può avvenire solo nel contesto del caricamento di un Menu
        // e la Section può essere già creata solo se il Menu è stato creato;
        // il controllo sul Menu avviene già in loadMenus
        Statement st = null;
        String query = "SELECT Sections.* FROM Sections WHERE Sections.menu=" + id + " ORDER BY Sections.position";

        try {
            st = this.connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                String name = rs.getString("name");
                int idSec = rs.getInt("id");

                Section sec = m.addSection(name);
                this.sectionObjects.put(sec, idSec);
                this.idToSectionObject.put(idSec, sec);
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

    }

    private User innerLoadUser(int userId) {
        // verifico se l'ho già caricato in precedenza
        User u = this.idToUserObject.get(userId);
        if (u != null) return u;

        Statement st = null;
        String query = "SELECT Users.id, Users.name, UserRoles.role FROM Users LEFT JOIN UserRoles on Users.id = " +
                "UserRoles.user where Users.id=" + userId;
        try {
            st = this.connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                if (u == null) {
                    u = new User(rs.getString("name"));
                    this.userObjects.put(u, userId);
                    this.idToUserObject.put(userId, u);
                }
                addUserRole(u, rs);
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

        return u;
    }

    private void addUserRole(User u, ResultSet rs) throws SQLException {
        char roleName = rs.getString("role").charAt(0);
        switch (roleName) {
            case 'c':
                u.addRole(User.Role.Cuoco);
                break;
            case 'h':
                u.addRole(User.Role.Chef);
                break;
            case 'o':
                u.addRole(User.Role.Organizzatore);
                break;
            case 's':
                u.addRole(User.Role.Servizio);
                break;
        }
    }
}
