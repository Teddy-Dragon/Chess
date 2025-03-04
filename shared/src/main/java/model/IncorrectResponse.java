package model;

public record IncorrectResponse(
        Boolean badRequest,
        Boolean unauthorized,
        Boolean alreadyTaken,
        String ErrorMessage
) {
}
