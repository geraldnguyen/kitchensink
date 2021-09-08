package example.service;

import example.entity.SampleEntity;
import example.entity.SampleRepository;
import org.springframework.stereotype.Service;

@Service
public class SampleService {
    private final SampleRepository sampleRepository;

    public SampleService(SampleRepository sampleRepository) {
        this.sampleRepository = sampleRepository;
    }

    public SampleEntity createSampleEntity() {
        SampleEntity sampleEntity = new SampleEntity();
        sampleEntity.setName("sample");

        return sampleRepository.save(sampleEntity);
    }
}
