package com.example.water.init;

import com.example.water.domain.analysis.entity.CognitiveDistortion;
import com.example.water.domain.analysis.repository.CognitiveDistortionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CognitiveDistortionInitializer implements CommandLineRunner {

    private final CognitiveDistortionRepository cognitiveDistortionRepository;

    @Override
    public void run(String... args) {
        if (cognitiveDistortionRepository.count() > 0) {
            return;
        }

        List<CognitiveDistortion> distortions = List.of(
                create(
                        "파국화",
                        "최악의 시나리오만 상상하며 현실을 과대평가",
                        "패닉셀"
                ),
                create(
                        "흑백 사고",
                        "중간 지대 없이 극단적으로만 판단",
                        "FOMO 추격매수"
                ),
                create(
                        "감정적 추론",
                        "감정을 객관적 근거로 착각",
                        "근거 없는 매도"
                ),
                create(
                        "확증 편향",
                        "자신의 결론을 지지하는 정보만 수집",
                        "과도한 집중투자"
                ),
                create(
                        "소외 불안 + 군집 행동",
                        "타인의 수익에서 나만 배제된다는 사회적 박탈감과 무리를 따라야 안전하다는 본능적 추종",
                        "고점 추격매수"
                ),
                create(
                        "통제의 환상",
                        "무작위적 시장 결과를 자신의 행동이 통제할 수 있다는 자기기만",
                        "과잉매매, 손절 실패"
                ),
                create(
                        "기준점 편향",
                        "처음 인식한 가격(최고점, 매수 단가)에 묶여 현재의 객관적 가치를 무시",
                        "펀더멘털 무시한 물타기"
                ),
                create(
                        "매몰비용 오류",
                        "이미 회수 불가능한 손실에 집착해 합리적 결정을 내리지 못함",
                        "손절 실패, 시드머니 묶임"
                )
        );

        cognitiveDistortionRepository.saveAll(distortions);
        System.out.println("인지 왜곡 더미데이터 8건 저장 완료");
    }

    private CognitiveDistortion create(String typeName, String description, String tag) {
        CognitiveDistortion distortion = new CognitiveDistortion();
        distortion.setTypeName(typeName);
        distortion.setDescription(description);
        distortion.setTag(tag);
        return distortion;
    }
}