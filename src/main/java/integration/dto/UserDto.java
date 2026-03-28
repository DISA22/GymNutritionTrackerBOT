package integration.dto;

public record UserDto(
        String name,
        Integer age,
        Double weight,
        Double height
) {
}
