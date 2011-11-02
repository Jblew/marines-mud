/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.world.beings;

import pl.jblew.code.libevent.EventListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import pl.jblew.code.jutils.utils.math.Interpolation1f;
import pl.jblew.code.jutils.utils.math.VariableFloat;
import marinesmud.world.Position;
import marinesmud.world.persistence.EnityManager;
import marinesmud.world.Race;
import marinesmud.world.World;
import marinesmud.world.area.room.Room;
import marinesmud.world.communication.Message;
import marinesmud.world.items.Item;
import marinesmud.world.persistence.WorldEnity;
import pl.jblew.code.jutils.utils.TextUtils;
import pl.jblew.code.jutils.utils.math.RangeF;
import pl.jblew.code.libevent.EventManager;

/**
 *
 * @author jblew
 */
public abstract class Being extends WorldEnity implements EventListener<Message> {
    @Persistent private String name = "being name";
    @Persistent private Race race = Race.getDefault();
    @Persistent private Position position = Position.STAND;
    @Persistent private VariableFloat health = new VariableFloat(1f, new RangeF(0, 1), new Interpolation1f() {
        public float calc(float x, float dt) {
            return x + 0.01f * dt;
        }
    });
    @Persistent private VariableFloat movement = new VariableFloat(1f, new RangeF(0, 1), new Interpolation1f() {
        public float calc(float x, float dt) {
            return x + 0.01f * dt;
        }
    });
    @Persistent private VariableFloat thirst = new VariableFloat(0f, new RangeF(0, 1), new Interpolation1f() {
        public float calc(float x, float dt) {
            return x + 0.005f * dt;
        }
    });
    @Persistent private VariableFloat starvation = new VariableFloat(0f, new RangeF(0, 1), new Interpolation1f() {
        public float calc(float x, float dt) {
            return x + 0.005f * dt;
        }
    });
    @Persistent private float money = 10f;
    @Persistent private int experiencePoints = 0;
    @Persistent private int level = 0;
    @Persistent private float intelligence = 0;
    @Persistent private float charisma = 0;
    @Persistent private float strength = 0;
    @Persistent private float condition = 0;
    @Persistent private float dexterity = 0;
    @Persistent private float wiseness = 0;
    @Persistent private float luck = 0;
    @Persistent private int room = 1;
    @Persistent private List<Item> inventory = new ArrayList<Item>();
    public transient final EventManager<Message> receivedMessagesEventManager = new EventManager<Message>();

    public Being() {
        super();
        _init();
    }

    public Being(int id) {
        super(id);
        _init();
    }

    public Being(int id, File f) {
        super(id, f);
        _init();
    }

    private void _init() {
        ((Room) getRoom()).messageEventManager.getListenersManager().addListener(this);
        World.getInstance().messagesEventManager.getListenersManager().addListener(this);
    }

    public synchronized String getName() {
        return name;
    }

    public synchronized String getFullName() {
        return TextUtils.ucfirst(getRace().name() + " " + getName().toLowerCase());
    }

    public synchronized void setName(String newName) {
        this.name = newName;
    }

    public synchronized Race getRace() {
        return race;
    }

    public synchronized Position getPosition() {
        return position;
    }

    public synchronized void setPosition(Position position) {
        this.position = position;
    }

    public synchronized float getHealth() {
        return health.getValue();
    }

    public synchronized float getMovement() {
        return movement.getValue();
    }

    public synchronized int getLevel() {
        return level;
    }

    public synchronized float getCondition() {
        return condition;
    }

    public synchronized float getInteligence() {
        return intelligence;
    }

    public synchronized float getStrength() {
        return strength;
    }

    public synchronized float getDexterity() {
        return dexterity;
    }

    public synchronized float getWiseness() {
        return wiseness;
    }

    public synchronized float getLuck() {
        return luck;
    }

    public synchronized float getCharisma() {
        return charisma;
    }

    public synchronized int getExperiencePoints() {
        return experiencePoints;
    }

    public synchronized int getMaxHealthPoints() {
        return (int) (level * (Math.pow(condition, 2) + 1) * getRace().getHealthConst());
    }

    public synchronized int getHealthPoints() {
        return (int) (getMaxHealthPoints() * health.getValue());
    }

    public synchronized int getMaxMovementPoints() {
        return (int) (level * (Math.pow(condition, 2) + 1) * getRace().getMovementConst());
    }

    public synchronized int getMovementPoints() {
        return (int) (getMaxMovementPoints() * movement.getValue());
    }

    public synchronized int getExperiencePointsNeededForNextLevel() {
        return (int) (25 * Math.pow(2 - Math.pow(intelligence, 5), level + 1));
    }

    public synchronized float getMoney() {
        return money;
    }

    public synchronized Room getRoom() {
        return Room.Manager.getInstance().getElement(room);
    }

    @Override
    public void actionPerformed(Message m) {
        if (m.canHear(this)) {
            this.receivedMessagesEventManager.fireEvent(m);
        }
    }

    public EnityManager<Being> getManager() {
        return Manager.getInstance();
    }

    @Override
    public long version() {
        return 10L;
    }

    @Override
    public String enityName() {
        return "being";
    }

    @Override
    public boolean needsCasting() {
        return true;
    }

    public static final class Manager extends EnityManager<Being> {
        private Manager() {
            super(BeingCaster.getInstance(), "being");
        }

        public static Manager getInstance() {
            return InstanceHolder.INSTANCE;
        }

        private static class InstanceHolder {
            public static final Manager INSTANCE = new Manager();
        }
    }
}
