import { Flower2, Heart } from 'lucide-react';

export default function AboutPage() {
  return (
    <div className="container py-12">
      <div className="mx-auto max-w-3xl">
        <div className="text-center">
          <Flower2 className="mx-auto mb-4 h-10 w-10 text-primary" />
          <h1 className="font-heading text-3xl font-bold text-heading md:text-4xl">Về Dear Floral</h1>
          <p className="mt-4 text-lg text-body leading-relaxed">
            Nơi nghệ thuật gặp thiên nhiên — mỗi cánh hoa là một kỷ niệm được lưu giữ
          </p>
        </div>

        <div className="mt-12 space-y-8 text-body leading-relaxed">
          <p>
            Dear Floral ra đời từ niềm đam mê với vẻ đẹp mong manh nhưng vĩnh cửu của hoa ép khô.
            Chúng tôi tin rằng mỗi bông hoa đều chứa đựng một câu chuyện — và nhiệm vụ của chúng tôi
            là lưu giữ câu chuyện đó mãi mãi.
          </p>
          <p>
            Từ những bó hoa cưới đầy ý nghĩa đến những bông hoa dại hái ven đường, mỗi tác phẩm
            của Dear Floral đều được tạo ra hoàn toàn bằng tay với sự tỉ mỉ và tình yêu.
          </p>

          <div className="grid gap-6 md:grid-cols-2">
            <div className="rounded-2xl bg-surface-warm p-6 text-center">
              <Heart className="mx-auto mb-3 h-8 w-8 text-accent" />
              <h3 className="font-heading text-lg font-semibold text-heading">Sứ mệnh</h3>
              <p className="mt-2 text-sm">Mang nghệ thuật hoa ép khô đến gần hơn với mọi người, biến mỗi khoảnh khắc thành tác phẩm nghệ thuật.</p>
            </div>
            <div className="rounded-2xl bg-surface-warm p-6 text-center">
              <Flower2 className="mx-auto mb-3 h-8 w-8 text-primary" />
              <h3 className="font-heading text-lg font-semibold text-heading">Giá trị</h3>
              <p className="mt-2 text-sm">Thủ công, chất lượng, bền vững và đầy cảm xúc trong từng sản phẩm.</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
