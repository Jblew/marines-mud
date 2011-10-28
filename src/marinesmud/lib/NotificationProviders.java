/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.lib;

import java.util.GregorianCalendar;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import marinesmud.Main;
import marinesmud.RunMode;
import marinesmud.system.Config;
import marinesmud.world.World;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author jblew
 */
public enum NotificationProviders {

    TWITTER(Dictionary.getDictionary("twitter messages")) {
        protected void abstractPostMessage(String content) {
            try {
                GregorianCalendar calendar = new GregorianCalendar();
                String add = calendar.get(GregorianCalendar.YEAR) + "/" + calendar.get(GregorianCalendar.MONTH) + "/" + calendar.get(GregorianCalendar.DAY_OF_MONTH) + " ";
                if (World.getInstance().twitterMessages.contains(add + content)) {
                    add += calendar.get(GregorianCalendar.HOUR) + ":" + calendar.get(GregorianCalendar.MINUTE) + "/" + calendar.get(GregorianCalendar.SECOND) + " ";
                }
                if (World.getInstance().twitterMessages.contains(add + content)) {
                    int num = 0;
                    while (World.getInstance().twitterMessages.contains(num + " " + add + content)) {
                        num++;
                    }
                    add = num + " " + add;
                }
                content = add + content;

                ConfigurationBuilder cb = new ConfigurationBuilder();
                String cKey = Config.get("twitter consumer key");
                String cSecret = Config.get("twitter consumer secret");
                String aToken = Config.get("twitter access token");
                String atSecret = Config.get("twitter access token secret");

                //System.out.println(cKey+" "+cSecret+" "+aToken+" "+atSecret);
                cb.setDebugEnabled(true).setOAuthConsumerKey(cKey).setOAuthConsumerSecret(cSecret).setOAuthAccessToken(aToken).setOAuthAccessTokenSecret(atSecret);
                cKey = null;
                cSecret = null;
                aToken = null;
                atSecret = null;

                World.getInstance().twitterMessages.add(content);
                Twitter twitter = new TwitterFactory(cb.build()).getInstance();
                Status status = twitter.updateStatus(content);
                Logger.getLogger("NotificationProviders.twitter").log(Level.INFO, "Twitter: Successfully updated the status to [{0}].", status.getText());
            } catch (TwitterException ex) {
                Logger.getLogger("NotificationProviders.twitter").log(Level.SEVERE, null, ex);
            }
        }
    };
    
    public final Dictionary dictionary;

    private NotificationProviders(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    protected abstract void abstractPostMessage(String key);

    public static void postMessage(String key, Map<String, String> data) {
        if (Manager.getInstance().notificationsOn) {
            for (NotificationProviders p : NotificationProviders.values()) {
                String msg = p.dictionary.getElement(key);

                for (String k : data.keySet()) {
                    String v = data.get(k);
                    msg = msg.replace("{" + k + "}", v);
                }

                p.abstractPostMessage(msg);
            }
        }
    }

    public static void postMessage(String key) {
        if (Manager.getInstance().notificationsOn) {
            for (NotificationProviders p : NotificationProviders.values()) {
                p.abstractPostMessage(p.dictionary.getElement(key));
            }
        }
    }

    public static boolean areEnabled() {
        return Manager.getInstance().notificationsOn;
    }

    private static class Manager {

        private final boolean notificationsOn;

        private Manager() {
            String sNotificationsOn = Config.get("notifications on");
            if (sNotificationsOn.equalsIgnoreCase("on")) {
                notificationsOn = true;
            } else if (sNotificationsOn.equalsIgnoreCase("off")) {
                notificationsOn = false;
            } else if (sNotificationsOn.equalsIgnoreCase("auto")) {
                notificationsOn = (Main.getRunMode() == RunMode.PRODUCTION);
            } else {
                throw new RuntimeException("Wrong value in config file config/config.yml in var 'notifications on'. Allowed values: on, off, auto.");
            }

            if (notificationsOn) {
                Logger.getLogger("NotificationProviders.twitter").log(Level.INFO, "Notifications are ENABLED.");
            } else {
                Logger.getLogger("NotificationProviders.twitter").log(Level.INFO, "Notifications are DISABLED.");
            }
        }

        private static Manager getInstance() {
            return InstanceHolder.INSTANCE;
        }

        private static class InstanceHolder {

            private static final Manager INSTANCE = new Manager();
        }
    }
}
