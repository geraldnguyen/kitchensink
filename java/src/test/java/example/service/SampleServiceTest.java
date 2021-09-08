package example.service;

import example.entity.SampleEntity;
import example.entity.SampleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SampleServiceTest {
    private SampleService sampleService;

    @Mock
    private SampleRepository sampleRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.sampleService = new SampleService(sampleRepository);
    }

    @Test
    void workHardMocking() {
        // arrange
        SampleEntity mockEntity = new SampleEntity();
        mockEntity.setId(1L);
        when(sampleRepository.save(any(SampleEntity.class))).thenReturn(mockEntity);

        // act
        SampleEntity sampleEntity = sampleService.createSampleEntity();

        // assert - the right entity returned
        assertEquals(mockEntity, sampleEntity);
        // assert - the right param passed
        ArgumentCaptor<SampleEntity> sampleEntityArgumentCaptor = ArgumentCaptor.forClass(SampleEntity.class);
        verify(sampleRepository).save(sampleEntityArgumentCaptor.capture());
        assertEquals("sample", sampleEntityArgumentCaptor.getValue().getName());
        assertNull(sampleEntityArgumentCaptor.getValue().getId());
    }

    @Test
    void workSmartMocking() {
        // arrange
        when(sampleRepository.save(any(SampleEntity.class))).thenAnswer(a -> {
            SampleEntity entity = a.getArgument(0);
            assertEquals("sample", entity.getName());
            assertNull(entity.getId());
            entity.setId(1L);
            return entity;
        });

        // act
        SampleEntity sampleEntity = sampleService.createSampleEntity();

        // assert - the right entity returned
        assertEquals(1L, sampleEntity.getId());
    }
}