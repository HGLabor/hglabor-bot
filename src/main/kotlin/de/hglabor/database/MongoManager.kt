package de.hglabor.database

import de.hglabor.BotClient
import de.hglabor.RoleButton
import de.hglabor.config.ConfigManager
import net.axay.blueutils.database.DatabaseLoginInformation
import net.axay.blueutils.database.mongodb.MongoDB

object MongoManager {

    val mongoDB = MongoDB(DatabaseLoginInformation(
        ConfigManager.databaseSettings.host ?: error(BotClient.logger.fatal("MongoDB login information cannot be null")),
        ConfigManager.databaseSettings.port ?: error(BotClient.logger.fatal("MongoDB login information cannot be null")),
        ConfigManager.databaseSettings.database ?: error(BotClient.logger.fatal("MongoDB login information cannot be null")),
        ConfigManager.databaseSettings.username ?: error(BotClient.logger.fatal("MongoDB login information cannot be null")),
        ConfigManager.databaseSettings.password ?: error(BotClient.logger.fatal("MongoDB login information cannot be null")),
    ))

    val roleButtons = mongoDB.getCollectionOrCreate<RoleButton>("hglaborBot_roleButtons")

}