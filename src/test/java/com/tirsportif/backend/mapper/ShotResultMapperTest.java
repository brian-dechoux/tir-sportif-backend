package com.tirsportif.backend.mapper;

import com.tirsportif.backend.dto.ParticipationResultsDto;
import com.tirsportif.backend.model.Discipline;
import com.tirsportif.backend.model.projection.ShotResultForShooterProjection;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Value
@AllArgsConstructor
class ShotResultForShooterProjectionImpl implements ShotResultForShooterProjection {
    Long participationId;
    boolean outrank;
    Integer serieNumber;
    Integer shotNumber;
    Double points;

    @Override
    public boolean getOutrank() {
        return outrank;
    }
}

public class ShotResultMapperTest {

    private final Discipline discipline = new Discipline(1L, "discipline", "DIS", 2, 2, false, 0, 10, Collections.emptySet());

    private ShotResultMapper shotResultMapper;

    @Before
    public void before() {
        shotResultMapper = new ShotResultMapper();
    }

    @Test
    public void _mapShooterResultToDto() {
        List<ShotResultForShooterProjection> projections = Arrays.asList(
                new ShotResultForShooterProjectionImpl(1L,false,1,0,1.),
                new ShotResultForShooterProjectionImpl(1L,false,1,1,1.),
                new ShotResultForShooterProjectionImpl(1L,false,2,0,2.),
                new ShotResultForShooterProjectionImpl(1L,false,2,1,2.)
        );

        List<ParticipationResultsDto> results = shotResultMapper.mapShooterResultToDto(projections, discipline);

        assertThat(results.get(0).getPoints()).hasSize(2);
        assertThat(results.get(0).getPoints().get(0)).hasSize(2);
        assertThat(results.get(0).getPoints().get(1)).hasSize(2);
    }

    @Test
    public void _mapShooterResultToDto_multipleParticipations() {
        List<ShotResultForShooterProjection> projections = Arrays.asList(
                new ShotResultForShooterProjectionImpl(2L,true,1,0,3.),
                new ShotResultForShooterProjectionImpl(2L,true,1,1,3.),
                new ShotResultForShooterProjectionImpl(2L,true,2,0,4.),
                new ShotResultForShooterProjectionImpl(2L,true,2,1,4.),
                new ShotResultForShooterProjectionImpl(3L,false,1,0,1.),
                new ShotResultForShooterProjectionImpl(3L,false,1,1,1.),
                new ShotResultForShooterProjectionImpl(3L,false,2,0,2.),
                new ShotResultForShooterProjectionImpl(3L,false,2,1,2.)
        );

        List<ParticipationResultsDto> results = shotResultMapper.mapShooterResultToDto(projections, discipline);

        assertThat(results).hasSize(2);
        assertThat(results.get(0).getParticipationReference().isOutrank()).isFalse();
        assertThat(results.get(1).getParticipationReference().isOutrank()).isTrue();
    }

    @Test
    public void _mapShooterResultToDto_specialCase_withTotalForSerie() {
        List<ShotResultForShooterProjection> projections = Arrays.asList(
                new ShotResultForShooterProjectionImpl(1L,false,1,null,9.),
                new ShotResultForShooterProjectionImpl(1L,false,2,null,8.)
        );

        List<ParticipationResultsDto> results = shotResultMapper.mapShooterResultToDto(projections, discipline);

        assertThat(results.get(0).getPoints().get(0).get(2)).isEqualTo(9);
        assertThat(results.get(0).getPoints().get(1).get(2)).isEqualTo(8);
    }
}
