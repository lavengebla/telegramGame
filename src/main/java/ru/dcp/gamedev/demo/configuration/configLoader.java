package ru.dcp.gamedev.demo.configuration;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.MERGE)
@Sources({"classpath:bot.properties",
        "classpath:game.properties",
          "system:properties"})
public interface configLoader extends Config {

//superUser
@Key("superUserId") int superUserId();
@Key("superUserName") String superUserName();
@Key("superUserUsername") String superUserUsername();
@Key("superUserSurname") String superUserSurname();
////admin чат
@Key("adminChat") String adminChat();
//параметры бота
@Key("botName") String botName();
@Key("botRuName") String botRuName();
@Key("botToken") String botToken();

@Key("base.stat.min") int getBaseStatMin();
@Key("base.stat.max") int getBaseStatMax();

}
