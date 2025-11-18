"""
store.sql 파일의 category를 categories로 변환하는 스크립트
- INSERT INTO test.store (category, ...) -> INSERT INTO test.store (categories, ...)
- 13,000개 쿼리를 자동으로 변환
"""

import re

def convert_category_to_categories(input_file, output_file):
    """
    category 컬럼을 categories로 변환
    """
    print(f"변환 시작: {input_file} -> {output_file}")

    converted_count = 0
    total_lines = 0

    try:
        with open(input_file, 'r', encoding='utf-8') as infile:
            with open(output_file, 'w', encoding='utf-8') as outfile:
                for line in infile:
                    total_lines += 1

                    # INSERT 문에서 category를 categories로 변환
                    if 'INSERT INTO test.store' in line and '(category,' in line:
                        # (category, -> (categories,
                        converted_line = line.replace('(category,', '(categories,')
                        outfile.write(converted_line)
                        converted_count += 1
                    else:
                        # 변환 불필요한 라인은 그대로 복사
                        outfile.write(line)

                    # 진행상황 출력 (1000개마다)
                    if total_lines % 1000 == 0:
                        print(f"진행 중... {total_lines}줄 처리, {converted_count}개 변환")

        print(f"\n✅ 변환 완료!")
        print(f"   총 {total_lines}줄 처리")
        print(f"   {converted_count}개 INSERT 문 변환")
        print(f"   결과 파일: {output_file}")

    except FileNotFoundError:
        print(f"❌ 에러: '{input_file}' 파일을 찾을 수 없습니다.")
    except Exception as e:
        print(f"❌ 에러 발생: {e}")

if __name__ == "__main__":
    # 파일 경로 설정
    input_file = "src/main/resources/store.sql"
    output_file = "src/main/resources/store_converted.sql"

    # 변환 실행
    convert_category_to_categories(input_file, output_file)

    print("\n📝 다음 단계:")
    print("   1. store_converted.sql 파일 확인")
    print("   2. 기존 store.sql 백업")
    print("   3. store_converted.sql을 store.sql로 이름 변경")
    print("   4. 애플리케이션 재시작")

