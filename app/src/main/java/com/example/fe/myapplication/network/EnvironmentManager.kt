package com.example.fe.myapplication.network


/**
 * @author Chris
 * @version 1.0
 * @since 2024/09/14
 */
object EnvironmentManager {

    val environment: IAppEnvironment by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        ServicesLoader.getService(IAppEnvironment::class.java, DefaultEnvironment::class.java)!!
    }

    // @Volatile
    // private var environment: IAppEnvironment? = null
    //
    // val appEnvironment: IAppEnvironment?
    //     get() {
    //         if (null == environment) {
    //             synchronized(AppConfigManager::class.java) {
    //                 if (null == environment) {
    //                     environment = ServicesLoader.getService(
    //                         IAppEnvironment::class.java,
    //                         DefaultEnvironment::class.java
    //                     )
    //                 }
    //             }
    //         }
    //         return environment
    //     }
}
