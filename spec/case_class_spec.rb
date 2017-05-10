require 'rspec'
require 'spec_helper.rb'

describe 'Case Classes' do

  before :all do

    module UnMixin
      def m1() 5 end
    end

    class UnaClasePadre
      def m2() 7 end
    end

    case_class UnaCaseClass < UnaClasePadre do
      include UnMixin
      attr_accessor :a, :b
      def self.m3() 9 end
    end

    case_class Saludador do
      def saludar
        'hola'
      end
    end

    @una_case_class = UnaCaseClass.new
    @un_saludador = Saludador.new

  end

  describe 'Funcionalidad basica' do


    it 'Validando metodos' do
      expect(@una_case_class.m1).to eql(5) # Retorna 5
      expect(@una_case_class.m2).to eql(7) # Retorna 7
      expect(UnaCaseClass.m3).to eql(9) # Retorna 9
      @un_saludador.should respond_to('saludar')
      expect(@un_saludador.saludar).to eql('hola')
    end

    it 'Deberia crear getters y no setters' do
      @una_case_class.should respond_to('a')
      @una_case_class.should_not respond_to('a=')
    end

    it 'Hereda de C' do
      expect(UnaCaseClass.superclass).to eql(UnaClasePadre)
    end

    it 'Es de la clase X' do
      expect(@una_case_class.class).to eql(UnaCaseClass)
    end

  end

  describe 'Funcion Constructora' do

    before :all do

      case_class Alumno do
        attr_accessor :nombre, :nota
        def hacer_trampa
          @nota = 10
        end
      end

      case_class Curso do
        attr_accessor :nombre, :codigo, :alumnos
      end

      @jose = Alumno("Jose", 8)
      @miguel = Alumno.new("Miguel", 2)

      @curso = Curso("TADP", "k3031", [@jose, @miguel])

    end

    it 'Instanciacion de una case class' do

      expect(@curso.nombre).to eql('TADP')
      expect(@curso.codigo).to eql('k3031')

      expect(@curso.alumnos[0].nombre).to eql('Jose')
      expect(@curso.alumnos[1].nota).to eql(2)

    end

    it 'Instancia inmutables' do

      @jose.should_not respond_to('nombre=')
      expect{@miguel.nombre = 10}.to raise_error(NoMethodError)
      expect{@miguel.hacer_trampa}.to raise_error(RuntimeError)

    end

  end

end

# describe 'Buenos Defaults' do
#
#   before do
#
#     case_class Persona do
#       attr_accessor :nombre, :apellido, :edad
#     end
#
#     case_class Curso do
#       attr_accessor :nombre, :codigo, :alumnos
#     end
#
#     case_class Alumno do
#       attr_accessor :nombre, :nota
#     end
#   end
#
#   describe 'Buenos Defaults' do
#     it 'to_s' do
#       persona_a = Persona.new('Juan', 'Carlos', 37)
#       persona_b = Persona.new('Juan', 'Carlos')
#       expect(persona_a.to_s).to eql('Persona(Juan, Carlos, 37)')
#       expect(persona_b.to_s).to eql('Persona(Juan, Carlos, nil)')
#       # en la definicion de to_s value recibe [persona_a, persona_b] y hace el to_s de ese array
#       # habria que iterar el array y a esos valores hacer .to_s (persona_a, persona_b y no su array)
#       curso_tadp = Curso.new('TADP', 514, [persona_a, persona_b])
#       expect(curso_tadp.to_s)
#           .to eql('Curso(TADP, 514, [Persona(Juan, Carlos, 37), Persona(Juan, Carlos, nil)])')
#     end
#
#     it '==' do
#
#       alumno = Alumno("Jose", 8)
#       otro_alumno_igual = Alumno("Jose", 8)
#       otro_alumno_distinto = Alumno("Miguel", 2)
#
#       expect(alumno == otro_alumno_igual).to eql(true)
#       expect(alumno == otro_alumno_distinto).to eql(false)
#
#     end
#
#     it 'hash' do
#       alumno = Alumno("Jose", 8)
#       hash_comun = 'Jose'.hash + 8.hash
#       expect(alumno.hash).to eql(-799690864674641430)
#       expect(alumno.hash == hash_comun).to eql(false)
#     end
#   end
#
#   before do
#
#     module M
#       def to_s()
#         "Soy un M"
#       end
#     end
#
#     class C
#       def to_s()
#         "Soy un C"
#       end
#     end
#
#     case_class X < C do
#       # no paso nada
#     end
#
#     case_class Y do
#         include M
#     end
#
#     case_class Z do
#       def to_s()
#         "Soy un Z"
#       end
#     end
#   end
#   describe 'Herencia en Buenos Defaults' do
#
#     expect(X().to_s).to eql('Soy un C')
#     expect(Y().to_s).to eql('Soy un M')
#     expect(Z().to_s).to eql('Soy un Z')
#
#   end
# end
#
# describe 'Copiado Inteligente' do
#
#   before do
#
#     case_class Alumno do
#       attr_accessor :nombre, :nota
#     end
#
#   end
#
#   describe 'Copiado Inteligente' do
#     it 'tests' do
#
#       alumno = Alumno("Jose", 8)
#       otro_alumno = alumno.copy
#       expect(alumno == otro_alumno).to eql(true)
#
#       otro_alumno = alumno.copy ->(nota){nota + 1}
#       expect(otro_alumno.nombre).to eql('Jose')
#       expect(otro_alumno.nota).to eql(9)
#
#       otro_alumno_mas = alumno.copy ->(nombre){"Arturo"}, ->(nota){5}
#       expect(otro_alumno_mas.nombre).to eql('Arturo')
#       expect(otro_alumno_mas.nota).to eql(5)
#
#       # MOSTRAR QUE LA SIGUIENTE LINEA DEBERIA DA ERROR
#       # alumno.copy ->(edad){25} # Error! No existe el atributo "edad"
#
#     end
#   end
# end
#
# describe 'Case Objects' do
#
#   before do
#
#     case_class Alumno do
#       attr_accessor :nombre, :estado
#     end
#
#     case_object Cursando do
#     end
#
#     case_class Termino do
#       attr_accessor :nota
#     end
#
#   end
#
#   describe 'Case Objects' do
#     it 'tests' do
#       # TODO: HACER ESTOS TESTS !
#
#       #expect().to eql()
#     end
#   end
# end
#
# describe 'Pattern Matching' do
#
#   before do
#
#     case_class Alumno do
#       attr_accessor :nombre, :estado
#     end
#
#     case_class Termino do
#       attr_accessor :nota
#     end
#
#     case_object Aprobo do
#     end
#
#   end
#
#   describe 'cualquier cosa' do
#     it 'test' do
#       alumno = Alumno("Jose", 8)
#       expect(_.=== alumno).to eql(true)
#       expect(_.=== 10).to eql(true)
#       expect(_.=== 'sarlanga').to eql(true)
#       expect(_.=== Object).to eql(true)
#     end
#   end
#
#   describe 'pertenece a un tipo' do
#     it 'test' do
#       alumno = Alumno("Jose", 9)
#       expect((is_a Array).=== alumno).to eql(false)
#       expect((is_a Alumno).=== alumno).to eql(true)
#     end
#   end
#
#   describe 'tener cierto valor en un atributo' do
#     it 'test' do
#       alumno = Alumno("Jose", 9)
#       expect((has(:nombre, "Raul")).=== alumno).to eql(false)
#       expect((has(:apellido, nil)).=== alumno).to eql(false)
#       expect((has(:nota, 9)).=== alumno).to eql(true)
#     end
#   end
#
#   describe 'comparacion estructural' do
#     it 'test' do
#       alumno = Alumno("Jose", Termino(9))
#       expect(Alumno(“Jose”, Termino(7)).=== alumno).to eql(false)
#       expect((Alumno(“Jose”, Aprobo)).=== alumno).to eql(false)
#       expect(Alumno(_, has(:nota, 9)).=== alumno).to eql(true)
#     end
#   end
# end

