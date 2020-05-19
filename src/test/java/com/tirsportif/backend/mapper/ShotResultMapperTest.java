package com.tirsportif.backend.mapper;

import com.tirsportif.backend.dto.ParticipationResultsDto;
import com.tirsportif.backend.model.projection.ShotResultForShooterProjection;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Value
@AllArgsConstructor
class ShotResultForShooterProjectionImpl implements ShotResultForShooterProjection {
    Long participationId;
    boolean outrank;
    int serieNumber;
    int shotNumber;
    double points;

    @Override
    public boolean getOutrank() {
        return outrank;
    }
}

public class ShotResultMapperTest {

    private ShotResultMapper shotResultMapper;

    @Before
    public void before() {
        shotResultMapper = new ShotResultMapper();
    }

    @Test
    public void _mapShooterResultToDto() {
        List<ShotResultForShooterProjection> projections = Arrays.asList(
                new ShotResultForShooterProjectionImpl(1L,true,1,1,1),
                new ShotResultForShooterProjectionImpl(1L,true,2,2,2),
                new ShotResultForShooterProjectionImpl(1L,false,3,3,3)
        );

        List<ParticipationResultsDto> results = shotResultMapper.mapShooterResultToDto(projections);

        assertThat(results).hasSize(2);
        assertThat(results.get(0).getResults()).hasSize(2);
        assertThat(results.get(1).getResults()).hasSize(1);
    }
}
