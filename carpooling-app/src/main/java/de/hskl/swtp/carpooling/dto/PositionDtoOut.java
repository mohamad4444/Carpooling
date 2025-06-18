package de.hskl.swtp.carpooling.dto;

public record PositionDtoOut(int id,String s, String s1) {
    public PositionDtoOut(String s, String s1) {
        this(0,s.toLowerCase(),s1);
    }
}
