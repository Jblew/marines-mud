/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.world.beings;

import java.io.File;
import java.io.Serializable;
import java.util.NoSuchElementException;
import marinesmud.lib.security.PasswordEncryptor;
import marinesmud.tap.CommunicationMode;

/**
 *
 * @author jblew
 */
public class Player extends Being implements Serializable {
    @Persistent private CommunicationMode preferredCommunicationMode = CommunicationMode.DEFAULT_MODE;
    @Persistent private String passwordHash = PasswordEncryptor.encrypt("test");
    @Persistent private boolean admin = false;
    private transient boolean loggedIn = false;
    private transient boolean playing = false;

    public Player(String name) {
        super();
        setName(name);
    }

    public Player() {
        super();
    }

    public Player(int id) {
        super(id);
    }

    public Player(int id, File f) {
        super(id, f);
    }

    public synchronized void play() {
        playing = true;
    }

    public synchronized void stopPlaying() {
        playing = false;
    }

    public synchronized boolean isPlaying() {
        return playing;
    }

    public synchronized void login(String login, String password) {
        if (getName().equals(login) && checkPassword(password)) {
            loggedIn = true;
        } else {
            throw new SecurityException("Password/login mismatch.");
        }
    }

    public synchronized void logout() {
        loggedIn = false;
    }

    public synchronized boolean checkPassword(String pass) {
        return PasswordEncryptor.encrypt(pass).equalsIgnoreCase(passwordHash);
    }

    public synchronized boolean isLoggedIn() {
        return loggedIn;
    }

    public synchronized boolean isAdmin() {
        return admin;
    }

    public synchronized CommunicationMode getPreferredCommunicationMode() {
        return preferredCommunicationMode;
    }

    public synchronized void setPreferredCommunicationMode(CommunicationMode communicationMode) {
        preferredCommunicationMode = communicationMode;
    }

    public static boolean exists(String name) {
        for (Being b : Player.Manager.getInstance().getElements()) {
            if (b instanceof Player) {
                Player p = (Player) b;
                if (p.getName().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Player getByName(String name) {
        for (Being b : Player.Manager.getInstance().getElements()) {
            if (b instanceof Player) {
                Player p = (Player) b;
                if (p.getName().equals(name)) {
                    return p;
                }
            }
        }
        throw new NoSuchElementException("Player^" + name);
    }

    @Override
    public String getCastTo() {
        return "player";
    }
}
