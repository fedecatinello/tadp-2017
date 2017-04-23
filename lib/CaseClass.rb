require_relative '../lib/modules/CaseClassMixin'

class CaseClass

  include CaseClassInstanceMixin

  @variables = nil

  def self.variables
    @variables
  end

  def self.attr_accessor (*attrs)
    @variables = attrs # Guardo los atributos en una variable de clase para poder pedirlas desde afuera
    attrs.map do |attr|
      self.send('attr_reader', attr)
      self.instance_variable_set('@' + attr.to_s, ':D')
    end
  end

end
