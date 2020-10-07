package ru.tpu.russian.back.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestUsersDto extends NotificationBaseRequestDto {

    private List<String> users;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < users.size(); i++) {
            if (i != users.size() - 1) {
                sb.append(users.get(i));
                sb.append(", ");
            } else {
                sb.append(users.get(i));
            }
        }
        sb.append(']');
        return sb.toString();
    }
}
