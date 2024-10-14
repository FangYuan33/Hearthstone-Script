package club.xiaojiawei.listener;

import club.xiaojiawei.ScriptApplication;
import club.xiaojiawei.bean.Release;
import club.xiaojiawei.data.ScriptStaticData;
import club.xiaojiawei.data.SpringData;
import club.xiaojiawei.utils.SystemUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.Properties;

import static club.xiaojiawei.data.ScriptStaticData.PROJECT_NAME;
import static club.xiaojiawei.enums.ConfigEnum.UPDATE_DEV;

/**
 * 脚本版本监听器，定时查看是否需要更新
 * @author 肖嘉威
 * @date 2023/9/17 21:49
 */
@Component
@Slf4j
public class VersionListener {

    @Getter
    private static Release latestRelease;
    @Getter
    private static Release currentRelease;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private SpringData springData;
    @Resource
    private Properties scriptConfiguration;
    private final static ReadOnlyBooleanWrapper canUpdate = new ReadOnlyBooleanWrapper(false);

    public static boolean isCanUpdate() {
        return canUpdate.get();
    }

    public static ReadOnlyBooleanProperty canUpdateReadOnlyProperty() {
        return canUpdate.getReadOnlyProperty();
    }

    @PostConstruct
    void init(){
          /*
            用idea启动时springData.getVersion()能读到正确的值
            打完包后启动this.getClass().getPackage().getImplementationVersion()能读到正确的值
        */
        String version = VersionListener.class.getPackage().getImplementationVersion();
        currentRelease = new Release();
        if (version == null){
            currentRelease.setTagName(version = springData.getVersion());
        }else {
            currentRelease.setTagName(version);
        }
        if (!version.endsWith("GA")){
            currentRelease.setPreRelease(true);
        }
    }

    @Scheduled(initialDelay = 500,fixedDelay = 1000 * 60 * 60 * 12)
    public void checkVersion(){
//        在idea中启动时就不要检查更新了
        if (!Objects.equals(Objects.requireNonNull(this.getClass().getResource("")).getProtocol(), "jar") && !ScriptApplication.getArgs().contains("--update")){
            return;
        }
        boolean updateDev = Objects.equals(scriptConfiguration.getProperty(UPDATE_DEV.getKey()), "true");
        log.info("开始从Gitee检查更新");
        log.info("更新dev：" + updateDev);
        try {
            if (updateDev){
                latestRelease = restTemplate.getForObject(String.format("https://gitee.com/api/v5/repos/zergqueen/%s/releases/latest", ScriptStaticData.PROJECT_NAME), Release.class);
            }else {
                Release[] releases = restTemplate.getForObject(String.format("https://gitee.com/api/v5/repos/zergqueen/%s/releases", ScriptStaticData.PROJECT_NAME), Release[].class);
                if (releases != null){
                    for (int i = releases.length - 1; i >= 0; i--) {
                        Release release = releases[i];
                        if (!release.isPreRelease()){
                            if (latestRelease == null || release.compareTo(latestRelease) > 0){
                                latestRelease = release;
                            }
                        }
                    }
                }
            }
        }catch (RuntimeException e){
            log.warn("从Gitee检查更新异常", e);
            log.info("开始从Github检查更新");
            try {
                if (updateDev){
                    Release[] releases = restTemplate.getForObject(String.format("https://api.github.com/repos/xjw580/%s/releases", ScriptStaticData.PROJECT_NAME), Release[].class);
                    if (releases != null && releases.length > 0){
                        latestRelease = releases[0];
                    }
                }else {
                    latestRelease = restTemplate.getForObject(String.format("https://api.github.com/repos/xjw580/%s/releases/latest", ScriptStaticData.PROJECT_NAME), Release.class);
                }
            }catch (RuntimeException e2){
                log.warn("从Github检查更新异常", e2);
            }
        }
        if (latestRelease != null){
            if (currentRelease.compareTo(latestRelease) < 0){
                canUpdate.set(true);
                log.info("有更新可用😊，当前版本：" + currentRelease + ", 最新版本：" + latestRelease);
                SystemUtil.notice(
                        String.format("发现新版本：%s", VersionListener.getLatestRelease().getTagName()),
                        String.format("更新日志：\n%s", VersionListener.getLatestRelease().getBody()),
                        "查看详情",
                        String.format("https://gitee.com/zergqueen/%s/releases/tag/%s", PROJECT_NAME, VersionListener.getLatestRelease().getTagName())
                );
            }else {
                canUpdate.set(false);
                log.info("已是最新，当前版本：" + currentRelease + ", 最新版本：" + latestRelease);
            }
        }else {
            canUpdate.set(false);
            log.warn("没有任何最新版本");
        }
    }

}
