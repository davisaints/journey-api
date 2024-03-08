package journey.miles.api.dto;

import jakarta.validation.constraints.NotNull;
import journey.miles.api.model.user.User;

public record UserDTOData(
        @NotNull String login,
        @NotNull String password) {
    public UserDTOData(User user) {
        this(user.getLogin(), user.getPassword());
    }
}
