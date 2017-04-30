require 'rspec'
require_relative('../lib/modules/SyntaxModule.rb')

describe 'Case Class' do

  before do

    case_class Persona do
      attr_accessor :nombre, :apellido, :edad
    end

    case_class Curso do
      attr_accessor :nombre, :codigo, :alumnos
    end

  end

  describe 'Buenos Defaults' do
    it 'to_s' do
      persona_a = Persona.new('Juan', 'Carlos', 37)
      persona_b = Persona.new('Juan', 'Carlos')
      expect(persona_a.to_s).to eql('Persona(Juan, Carlos, 37)')
      expect(persona_b.to_s).to eql('Persona(Juan, Carlos, nil)')
      # en la definicion de to_s value recibe [persona_a, persona_b] y hace el to_s de ese array
      # habria que iterar el array y a esos valores hacer .to_s (persona_a, persona_b y no su array)
      curso_tadp = Curso.new('TADP', 514, [persona_a, persona_b])
      expect(curso_tadp.to_s)
          .to eql('Curso(TADP, 514, [Persona(Juan, Carlos, 37), Persona(Juan, Carlos, nil)])')
    end
  end
end