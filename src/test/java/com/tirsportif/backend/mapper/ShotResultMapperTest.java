package com.tirsportif.backend.mapper;

import com.tirsportif.backend.dto.GetParticipationResultsResponse;
import com.tirsportif.backend.model.Discipline;
import com.tirsportif.backend.model.projection.ShotResultProjection;
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
class ShotResultProjectionImpl implements ShotResultProjection {
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

// TODO Correct it, use real examples
public class ShotResultMapperTest {

    private final Discipline discipline = new Discipline(1L, "discipline", "DIS", 2, 2, false, 0, 10, Collections.emptySet());

    private ShotResultMapper shotResultMapper;

    @Before
    public void before() {
        shotResultMapper = new ShotResultMapper();
    }

    @Test
    public void _mapShooterResultToDto() {
        List<ShotResultProjection> projections = Arrays.asList(
                new ShotResultProjectionImpl(1L,false,1,0,1.),
                new ShotResultProjectionImpl(1L,false,1,1,1.),
                new ShotResultProjectionImpl(1L,false,2,0,2.),
                new ShotResultProjectionImpl(1L,false,2,1,2.)
        );

        List<GetParticipationResultsResponse> results = shotResultMapper.mapResultToDto(projections, discipline);

        assertThat(results.get(0).getPoints()).hasSize(2);
        assertThat(results.get(0).getPoints().get(0)).hasSize(2);
        assertThat(results.get(0).getPoints().get(1)).hasSize(2);
    }

    @Test
    public void _mapShooterResultToDto_multipleParticipations() {
        List<ShotResultProjection> projections = Arrays.asList(
                new ShotResultProjectionImpl(2L,true,1,0,3.),
                new ShotResultProjectionImpl(2L,true,1,1,3.),
                new ShotResultProjectionImpl(2L,true,2,0,4.),
                new ShotResultProjectionImpl(2L,true,2,1,4.),
                new ShotResultProjectionImpl(3L,false,1,0,1.),
                new ShotResultProjectionImpl(3L,false,1,1,1.),
                new ShotResultProjectionImpl(3L,false,2,0,2.),
                new ShotResultProjectionImpl(3L,false,2,1,2.)
        );

        List<GetParticipationResultsResponse> results = shotResultMapper.mapResultToDto(projections, discipline);

        assertThat(results).hasSize(2);
        assertThat(results.get(0).getParticipationReference().isOutrank()).isFalse();
        assertThat(results.get(1).getParticipationReference().isOutrank()).isTrue();
    }

    @Test
    public void _mapShooterResultToDto_specialCase_withTotalForSerie() {
        List<ShotResultProjection> projections = Arrays.asList(
                new ShotResultProjectionImpl(1L,false,1,null,9.),
                new ShotResultProjectionImpl(1L,false,2,null,8.)
        );

        List<GetParticipationResultsResponse> results = shotResultMapper.mapResultToDto(projections, discipline);

        assertThat(results.get(0).getPoints().get(0).get(2)).isEqualTo(9);
        assertThat(results.get(0).getPoints().get(1).get(2)).isEqualTo(8);
    }
}
