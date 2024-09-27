package org.zubova.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Entity {
    private Integer id;
    @Builder.Default
    private String title = "Заголовок";
    @Builder.Default
    private Boolean verified = true;
    @Builder.Default
    private Addition addition = Addition.builder().build();
    @Builder.Default
    @JsonProperty("important_numbers")
    private List<Integer> importantNumbers = Arrays.asList(1, 2, 3);

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Addition {
        private Integer id;
        @Builder.Default
        @JsonProperty("additional_info")
        private String additionalInfo = "Дополнительная информация";
        @Builder.Default
        @JsonProperty("additional_number")
        private Integer additionalNumber = 2;
    }
}
