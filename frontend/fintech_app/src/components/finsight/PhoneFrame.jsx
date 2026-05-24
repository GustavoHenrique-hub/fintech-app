// PhoneFrame: "moldura" visual que simula um celular ao redor da aplicação.
//
// Responsividade:
//  - Em telas pequenas (< sm = 640px) ocupamos a viewport inteira (sem moldura),
//    porque o usuário já está em um celular real — não faz sentido encolher.
//  - Em telas médias/grandes mostramos a moldura centralizada (vitrine/demo).
export const PhoneFrame = ({ children }) => {
  return (
    <div className="min-h-screen w-full bg-[#0c0d10] flex items-center justify-center sm:p-6 md:p-8">
      <div
        className="
          relative w-full bg-background overflow-hidden flex flex-col
          h-screen sm:h-auto
          sm:max-w-[390px] sm:aspect-[9/19.5]
          sm:rounded-[2.75rem] sm:border-[10px] sm:border-[#0a0a0d]
          sm:shadow-[0_30px_80px_-20px_rgba(0,0,0,0.6),0_0_0_1px_rgba(255,255,255,0.04)]
        "
      >
        {/* Notch (entalhe superior) — só faz sentido na moldura desktop. */}
        <div className="hidden sm:block absolute top-1.5 left-1/2 -translate-x-1/2 w-24 h-5 rounded-full bg-[#0a0a0d] z-30" />
        {/* Container interno empilha header, tela ativa e bottom-nav verticalmente. */}
        <div className="absolute inset-0 flex flex-col">{children}</div>
      </div>
    </div>
  );
};
