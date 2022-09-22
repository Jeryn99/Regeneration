package mc.craig.software.regen.util.constants;

public class RMessages {
    public static final String REGENERATION_DEATH_MSG = createMessage("regeneration_death_message");
    public static final String TIMELORD_STATUS = createMessage("timelord_status");
    public static final String GAINED_REGENERATIONS = createMessage("gained_regens");
    public static final String END_OF_PROCESS = createMessage("end_of_process");
    public static final String PUNCH_WARNING = createMessage("punch_warning");
    public static final String POST_REDUCED_DAMAGE = createMessage("post_reduced_damage");
    public static final String DELAYED_REGENERATION = createMessage("delayed_regeneration");
    public static final String TRANSFER_MAX_REGENS = createMessage("transfer.max_regens");
    public static final String TRANSFER_SUCCESSFUL = createMessage("transfer.successful");
    public static final String TRANSFER_EMPTY_WATCH = createMessage("transfer.empty_watch");

    public static final String TRANSFER_FULL_WATCH = createMessage("transfer.full_watch");
    public static final String TRANSFER_NO_REGENERATIONS = createMessage("transfer.no_regenerations");
    public static final String TRANSFER_INVALID_STATE = createMessage("transfer.invalid_state");

    public static final String FAST_FORWARD_CMD_FAIL = createCommand("fast_forward_cmd_fail");
    public static final String SET_TRAIT_SUCCESS = createCommand("set_trait.success");
    public static final String SET_TRAIT_ERROR = createCommand("set_trait.error");
    public static final String SET_REGEN_SUCCESS = createCommand("set_regen.success");
    public static final String SET_REGEN_CONFIG_DISABLED = createCommand("set_regen.config_disabled");
    public static final String SET_REGEN_INVALID_ENTITY = createCommand("set_regen.invalid_entity");


    public static String createMessage(String message) {
        return "message.regen." + message;
    }
    public static String createCommand(String message) {
        return "command.regen." + message;
    }
}